using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Office = Microsoft.Office.Core;
using VBA = Microsoft.Vbe.Interop;
using Visio = Microsoft.Office.Interop.Visio;

namespace pmmining
{
    public class ProcessModelsMining
    {
        private static string PAGES_PATH = @"D:\GitHub\research-calculations\research-calculations-java\portal\pages\";

        private static string MODELS_PATH = @"D:\GitHub\research-calculations\research-calculations-java\portal\pages\models\";

        private static string PORTAL_COLLECTION_PATH = @"D:\GitHub\research-calculations\research-calculations-java\src\main\resources\repository\PortalProcessModelsCollection.java";

        public static void Main(string[] args)
        {
            Console.WriteLine("Process models mining started...");

            string[] models = Directory.GetFiles(MODELS_PATH, "*.vsd", SearchOption.AllDirectories);

            foreach (string model in models)
            {
                Console.WriteLine(model);

                Visio.Application visioApp = new Visio.Application();
                Visio.Document document = visioApp.Documents.Open(model);

                visioApp.Visible = true;

                VBA.VBComponent codeModule = document.VBProject.VBComponents.Add(VBA.vbext_ComponentType.vbext_ct_StdModule);

                string vbaCode = @"
                Sub ExtractProcessModel()
                    Set file = CreateObject(""Scripting.FileSystemObject"").CreateTextFile(""${models_path}"" & ActiveDocument.Name & "".java"", True, True)
    
                    file.Write (""/* "" & ActiveDocument.Name & "" */ {"" & vbNewLine)
                    file.Write (""Process process = repository.createProcess("""""" & ActiveDocument.Name & """""");"" & vbNewLine)
    
                    For i = 1 To ActivePage.Shapes.Count
                        'For native Visio-based EPC diagrams
                        If InStr(ActivePage.Shapes(i).NameU, ""Dynamic connector"") <> 0 Then
                            FromShape = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(1).ToSheet.Text
                            ToShape = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(2).ToSheet.Text
            
                            FromShapeID = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(1).ToSheet.ID
                            ToShapeID = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(2).ToSheet.ID
            
                            FromShapeName = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(1).ToSheet.NameU
                            ToShapeName = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(2).ToSheet.NameU
            
                            If InStr(FromShapeName, ""Organizational unit"") <> 0 Then
                                file.Write (""repository.createResourceFlow("")
                            ElseIf InStr(FromShapeName, ""Information/ Material"") <> 0 Then
                                file.Write (""repository.createInputFlow("")
                            ElseIf InStr(ToShapeName, ""Information/ Material"") <> 0 Then
                                file.Write (""repository.createOutputFlow("")
                            Else
                                file.Write (""repository.createControlFlow("")
                            End If
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                CurrentShapeName = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.NameU
            
                                ResText = Null
                                ResID = Null
                
                                If j = 1 Then
                                    ResText = FromShape
                                    ResID = FromShapeID
                                Else
                                    ResText = ToShape
                                    ResID = ToShapeID
                                End If
            
                                If InStr(CurrentShapeName, ""Event"") <> 0 Then
                                    file.Write (""repository.createEvent("""""" & ResText & """""", process)"")
                                End If
                
                                If InStr(CurrentShapeName, ""Function"") <> 0 Then
                                    file.Write (""repository.createFunction("""""" & ResText & """""", process)"")
                                End If
                
                                If InStr(CurrentShapeName, ""XOR"") <> 0 Then
                                    file.Write (""repository.createXOrGateway("""""" & ResID & """""", process)"")
                                ElseIf InStr(CurrentShapeName, ""OR"") <> 0 Then
                                    file.Write (""repository.createOrGateway("""""" & ResID & """""", process)"")
                                End If
                
                                If InStr(CurrentShapeName, ""AND"") <> 0 Then
                                    file.Write (""repository.createAndGateway("""""" & ResID & """""", process)"")
                                End If
                
                                If InStr(CurrentShapeName, ""Process path"") <> 0 Then
                                    file.Write (""repository.createProcessInterface("""""" & ResText & """""", process)"")
                                End If
                
                                If InStr(CurrentShapeName, ""Organizational unit"") <> 0 Then
                                    file.Write (""repository.createOrganizationalUnit("""""" & ResText & """""")"")
                                End If
                
                                If InStr(CurrentShapeName, ""Information/ Material"") <> 0 Then
                                    file.Write (""repository.createBusinessObject("""""" & ResText & """""")"")
                                End If
                
                                If j = 1 Then
                                    file.Write ("", "")
                                Else
                                    file.Write ("");"" & vbNewLine)
                                End If
                            Next
                        End If

                        'For custom ARIS stencil-based diagrams
                        If InStr(ActivePage.Shapes(i).Name, ""IsPredecessorOfARIS"") <> 0 Then
                            Subj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(1).ToSheet.Text
                            Obj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(2).ToSheet.Text
            
                            file.Write (""repository.createControlFlow("")
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                ResName = Null
                
                                If j = 1 Then
                                    ResName = Subj
                                Else
                                    ResName = Obj
                                End If
                
                                ValID = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.ID
                
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""EventARIS"") <> 0 Then
                                    file.Write (""repository.createEvent("""""" & ResName & """""", process)"")
                                End If
                
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""FunctionARIS"") <> 0 Then
                                    file.Write (""repository.createFunction("""""" & ResName & """""", process)"")
                                End If
                
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""ProcessInterfaceARIS"") <> 0 Then
                                    file.Write (""repository.createProcessInterface("""""" & ResName & """""", process)"")
                                End If
                
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""ANDruleARIS"") <> 0 Then
                                    file.Write (""repository.createAndGateway("""""" & ValID & """""", process)"")
                                End If
                
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""XORruleARIS"") <> 0 Then
                                    file.Write (""repository.createXOrGateway("""""" & ValID & """""", process)"")
                                ElseIf InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""ORruleARIS"") <> 0 Then
                                    file.Write (""repository.createOrGateway("""""" & ValID & """""", process)"")
                                End If
                
                                If j = 1 Then
                                    file.Write ("", "")
                                Else
                                    file.Write ("");"" & vbNewLine)
                                End If
                            Next
                        End If
    
                        If InStr(ActivePage.Shapes(i).Name, ""RoleInProcessARIS"") <> 0 Then
                            Subj = Null
                            Obj = Null
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""FunctionARIS"") <> 0 Then
                                    Subj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                Else
                                    Obj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                End If
                            Next
            
                            file.Write (""repository.assignOrganizationalUnits(repository.createFunction("""""" & Subj & """""", process), repository.createOrganizationalUnit("""""" & Obj & """"""));"" & vbNewLine)
                        End If
        
                        If InStr(ActivePage.Shapes(i).Name, ""SoftwareRoleARIS"") <> 0 Then
                            Subj = Null
                            Obj = Null
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""FunctionARIS"") <> 0 Then
                                    Subj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                Else
                                    Obj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                End If
                            Next
            
                            file.Write (""repository.assignApplicationSystems(repository.createFunction("""""" & Subj & """""", process), repository.createApplicationSystem("""""" & Obj & """"""));"" & vbNewLine)
                        End If
        
                        If InStr(ActivePage.Shapes(i).Name, ""InputObjectARIS"") <> 0 Then
                            Subj = Null
                            Obj = Null
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""FunctionARIS"") <> 0 Then
                                    Subj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                Else
                                    Obj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                End If
                            Next
            
                            file.Write (""repository.assignInputs(repository.createFunction("""""" & Subj & """""", process), repository.createBusinessObject("""""" & Obj & """"""));"" & vbNewLine)
                        End If
        
                        If InStr(ActivePage.Shapes(i).Name, ""OutputObjectARIS"") <> 0 Then
                            Subj = Null
                            Obj = Null
            
                            For j = 1 To ActivePage.Shapes(i).FromConnects.ToSheet.Connects.Count
                                If InStr(ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Name, ""FunctionARIS"") <> 0 Then
                                    Subj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                Else
                                    Obj = ActivePage.Shapes(i).FromConnects.ToSheet.Connects(j).ToSheet.Text
                                End If
                            Next
            
                            file.Write (""repository.assignOutputs(repository.createFunction("""""" & Subj & """""", process), repository.createBusinessObject("""""" & Obj & """"""));"" & vbNewLine)
                        End If
                    Next
    
                    file.Write (""repository.store();"" & vbNewLine)
                    file.Write (""}"")
    
                    file.Close
                End Sub";

                vbaCode = vbaCode.Replace("${models_path}", MODELS_PATH);

                codeModule.CodeModule.AddFromString(vbaCode);

                document.ExecuteLine("ExtractProcessModel");

                document.VBProject.VBComponents.Remove(codeModule);

                document.Save();
                document.Close();

                visioApp.Quit();
            }

            string[] files = Directory.GetFiles(MODELS_PATH, "*.java", SearchOption.AllDirectories);

            string modelsCollection = @"
            package main.resources.repository;

            import edu.kopp.phd.ProcessModelRepositoryApplication;
            import edu.kopp.phd.model.flow.Process;
            import edu.kopp.phd.repository.RDFRepository;

            import org.junit.Test;

            public class PortalProcessModelsCollection {
            public RDFRepository repository = RDFRepository.getInstance();
            ";

            foreach (string file in files)
            {
                modelsCollection += File.ReadAllText(file) + "\n";

                File.Delete(file);
            }

            modelsCollection += @"
                @Test
                public void deploy() {
                    ProcessModelRepositoryApplication.run();
                }
            }";

            File.WriteAllText(PORTAL_COLLECTION_PATH, modelsCollection);

            string[] pages = Directory.GetFiles(PAGES_PATH, "*.html", SearchOption.AllDirectories);

            foreach (string page in pages)
            {
                File.Delete(page);
            }

            Console.WriteLine("Done!");
        }
    }
}
