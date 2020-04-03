cat("\f")
setwd("D:/GitHub/research-calculations/research-calculations-r")

library(readxl)
library(party)
library(caret)
library(rpart)
library(rpart.plot)

training <- read_excel("BPMN_Quality_DataSet.xlsx")
training$status <- factor(training$status)

# Logit (basic metrics)
model <- glm(nstatus ~ totalNodes + events + totalGateways, 
             data = training)

print(summary(model))

prediction <- predict(model, newdata = training, type = "response")

confusionMatrix(as.factor(prediction < 0.5), as.factor(training$nstatus < 1))

# DTree (basic metrics)
tree <- rpart(status ~ totalNodes + events + totalGateways, 
              data = training, method = "class")

rpart.plot(tree)
print(tree)

pred <- predict(tree, type="class")

confusionMatrix(as.factor(pred), as.factor(training$status))

# Logit (calculated metrics)
model <- glm(nstatus ~ invalidNodes + startEvents + endEvents + unmatchedGateways + 
               orGateways, 
             data = training)

print(summary(model))

prediction <- predict(model, newdata = training, type = "response")

confusionMatrix(as.factor(prediction < 0.5), as.factor(training$nstatus < 1))

# DTree (calculated metrics)
tree <- rpart(status ~ invalidNodes + startEvents + endEvents + unmatchedGateways + 
                orGateways, 
              data = training, method = "class")

rpart.plot(tree)
print(tree)

pred <- predict(tree, type="class")

confusionMatrix(as.factor(pred), as.factor(training$status))
