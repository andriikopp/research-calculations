cat("\f")
setwd("D:/GitHub/research-calculations/research-calculations-r")

library(readxl)
library(party)
library(caret)
library(rpart)
library(rpart.plot)

training <- read_excel("BPMN_Quality_DataSet.xlsx")
training$status <- factor(training$status)

# Logit
model <- glm(nstatus ~ totalNodes + startEvents + endEvents + totalGateways, 
             data = training)

print(summary(model))

prediction <- predict(model, newdata = training, type = "response")

confusionMatrix(as.factor(prediction < 0.5), as.factor(training$nstatus < 1))

# DTree
tree <- rpart(status ~ totalNodes + startEvents + endEvents + totalGateways, 
              data = training, method = "class")

rpart.plot(tree)
print(tree)

pred <- predict(tree, type="class")

confusionMatrix(as.factor(pred), as.factor(training$status))
