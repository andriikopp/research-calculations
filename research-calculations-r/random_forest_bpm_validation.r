library(readxl)
library(randomForest)

training <- read_excel("training.xlsx")

training$invalid <- factor(training$invalid)

print(randomForest(invalid ~ density, 
                   data = training))
print(randomForest(invalid ~ connectivity, 
                   data = training))
print(randomForest(invalid ~ balance, 
                   data = training))
print(randomForest(invalid ~ csc, 
                   data = training))
