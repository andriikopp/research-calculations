setwd("D:/GitHub/research-calculations/research-calculations-r")

# 
# Jaccard similarity index
#
jaccard <- function(first, second) {
  union_size <- length(union(first, second))
  intersection_size <- length(intersect(first, second))
  
  return(intersection_size / union_size)
}

# Flow objects of the first BPM
flow_objects_first <- c("Out of stock", "Place order", "Order placed", 
                        "Sign contract", "Contract signed", "Receive goods", 
                        "Goods received", "Verify invoice", "Invoice verified")

# Flow objects of the second BPM
flow_objects_second <- c("Out of stock", "Place order", "Order placed",
                         "Receive goods", "Goods received", "AND-split",
                         "Store goods", "Verify invoice", "AND-join", 
                         "Goods supplied")

# Organizational units of the first BPM
org_units_first <- c("Supply Dept.", "Warehouse", "Accounting Dept.")

# Organizational units of the second BPM
org_units_second <- c("Supply Dept.", "Warehouse", "Accounting Dept.")

# IT-systems of the first BPM
apps_first <- c("SRM", "WM")

# IT-systems of the second BPM
apps_second <- c("SRM", "WM")

#
# Calculate label similarities
#
org_units_lab <- jaccard(org_units_first, org_units_second)
apps_lab <- jaccard(apps_first, apps_second)
flow_objects_lab <- jaccard(flow_objects_first, flow_objects_second)

# Flow objects statements of the first BPM
#
# For convinience the labels are shortened:
# i.e.: "Out of stock" -> "OOS",
#       "Place order" -> "PO", etc.
#
flow_statements_first <- c("OOS,PO", "PO,OP", "OP, SC", "SC,CS", "CS,RG", "RG,GR",
                           "GR,VI", "VI,IV")

# Flow objects statements of the second BPM
flow_statements_second <- c("OOS,PO", "PO,OP", "OP,RG", "RG,GR", "GR,A-s",
                            "A-s,SG", "A-s,VI", "SG,A-j", "VI,A-j", "A-j,GS")

# Organizational untis statements of the first BPM
org_statements_first <- c("PO,SD", "SC,SD", "RG,W", "VI,AD")

# Organizational units statements of the second BPM
org_statements_second <- c("PO,SD", "RG,W", "SG,W", "VI,AD")

# IT-systems statements of the first BPM
apps_statements_first <- c("PO,SRM", "RG,SRM", "RG,WM")


# IT-systems statements of the second BPM
apps_statements_second <- c("PO,SRM", "RG,SRM", "RG,WM", "SG,WM")

#
# Calculate structure similarities
#
org_units_str <- jaccard(org_statements_first, org_statements_second)
apps_str <- jaccard(apps_statements_first, apps_statements_second)
flow_objects_str <- jaccard(flow_statements_first, flow_statements_second)

#
# Weights of the process domains
#
org_weight <- 0.33
apps_weight <- 0.33
flow_objects_weight <- 0.34

#
# Weights of the similarities
#
lab_weight <- 0.5
str_weight <- 0.5

#
# Calculate the total similarity of BPMs
#
similarity <- org_weight * (lab_weight * org_units_lab + str_weight * org_units_str) + 
  apps_weight * (lab_weight * apps_lab + str_weight * apps_str) +
  flow_objects_weight * (lab_weight * flow_objects_lab + str_weight * flow_objects_str)
