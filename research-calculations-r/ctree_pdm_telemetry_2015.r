cat("\f")

setwd("D:/GitHub/research-calculations/research-calculations-r")

library(readxl)
library(party)
library(rpart)
library(rpart.plot)

training <- read.csv("pdm_telemetry_2015_components.csv")

tree <- rpart(failure ~ volt + rotate + pressure + vibration, 
              data = training, method = "class")

rpart.plot(tree)
