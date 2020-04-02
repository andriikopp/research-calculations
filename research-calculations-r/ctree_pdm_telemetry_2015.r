cat("\f")
setwd("D:/GitHub/research-calculations/research-calculations-r")

library(readxl)
library(party)
library(rpart)
library(rpart.plot)
library(caret)

training <- read.csv("pdm_telemetry_2015.csv")

tree <- rpart(failure ~ volt + rotate + pressure + vibration, 
              data = training, method = "class")

rpart.plot(tree)

pred <- predict(tree, type="class")

confusionMatrix(as.factor(pred), as.factor(training$failure))
