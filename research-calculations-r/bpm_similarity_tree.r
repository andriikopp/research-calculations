setwd("D:/GitHub/research-calculations/research-calculations-r")

library(readxl)
library(party)

# Load the training dataset to build Decision Tree Model
training <- read_excel("bpm_sim_training.xlsx")

# Encode string values as categories
training$evaluation <- factor(training$evaluation)
training$application <- factor(training$application)

# Build Decision Tree Model
tree <- ctree(application ~ similarity, 
              data = training)

# Display the tree
plot(tree)

# Load the test dataset
test <- read_excel("bpm_sim_test.xlsx")

# Use the decision tree to predict classes of similarity
prediction <- predict(tree, test)

# Display prediction results
print(prediction)

