import cv2
import numpy as np
import os

# input directory with process diagrams
input_dir = 'input'

# read process diagram file names
diagrams = os.listdir(input_dir)

# for OpenCV template matching
threshold = 0.9

# color of the found template
matching_color = (0, 0, 255)

# iterate over all input diagrams
for diagram in diagrams:
    print('Diagram file name\t' + diagram)

    # open source process diagram, make it gray, and blur it
    img_rgb = cv2.imread(input_dir + '/' + diagram)
    img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)
    img_gray = cv2.GaussianBlur(img_gray, (3, 3), 0)

    # templates for matching
    # each has name, number of occurrence, path, color
    templates = [
        ['xor', 0, 'xor.png'],
        ['or', 0, 'or.png'],
        ['and', 0, 'and.png'],
        ['function', 0, 'function.png'],
        ['event', 0, 'event.png']
    ]

    for path in templates:
        # open template image, make it gray, and blur it
        template = cv2.imread('templates/' + path[2])
        template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
        template = cv2.GaussianBlur(template, (3, 3), 0)

        # get width and height of the template
        w, h = template.shape[::-1]

        # detect all matching areas and filter them using threshold
        res = cv2.matchTemplate(img_gray, template, cv2.TM_CCOEFF_NORMED)
        loc = np.where(res >= threshold)

        # list of points without collisions
        unique_points = []

        for pt in zip(*loc[::-1]):
            if len(unique_points) == 0:
                # store first found point of matched area
                unique_points.append(pt)
            else:
                unique = True

                for upt in unique_points:
                    # calculate distance for each next point found by OpenCV template matching
                    dist = np.sqrt((upt[0] - pt[0]) ** 2 + (upt[1] - pt[1]) ** 2)

                    # check for already stored similar points
                    if dist <= threshold * 10:
                        unique = False
                        break

                if unique:
                    # store point if it neighbours are not stored
                    unique_points.append(pt)

        for pt in unique_points:
            # show found areas on the image
            cv2.rectangle(img_rgb, pt, (pt[0] + w, pt[1] + h), matching_color, 2)

            # put text to display the type of found element
            cv2.putText(img_rgb, path[0], (pt[0] + w, pt[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.7, matching_color, 2)

            path[1] += 1

    # store processed image
    cv2.imwrite('output/' + diagram, img_rgb)

    # print found templates
    for path in templates:
        print(path[0] + '\t' + str(path[1]))

    # get values of certain metric numbers
    xor_conns = templates[0][1]
    or_conns = templates[1][1]
    and_conns = templates[2][1]
    functions = templates[3][1]
    events = templates[4][1]

    # calculate the general control-flow complexity coefficient
    cfc_general = xor_conns * 2 + or_conns * 4 + and_conns

    print('Control-flow complexity\t' + str(cfc_general))

    # there are 5 levels of understandability of process models
    # 1 is the best, 5 is the worst
    fuzzy_levels = []

    # threshold values of (xor, or, and) connectors
    # to check correspondence of a model to a certain level
    cfc_thresholds = [
        [6, 1, 1],
        [12, 2, 1],
        [22, 6, 3],
        [31, 9, 4],
        [46, 14, 7]
    ]

    # used to store correspondence of the diagram to each level
    fuzzy_levels = []

    for level in range(0, len(cfc_thresholds)):
        fuzzy_values = []

        # calculate membership values for each level
        for i in range(0, len(cfc_thresholds[level])):
            fuzzy_values.append(1 / (1 + (templates[i][1] - cfc_thresholds[level][i]) ** 2))

        # store min value multiplied by result number of a level
        fuzzy_levels.append(np.amin(fuzzy_values) * (1 + level))

    # use Max-Criterion rule to get the understandability level
    level = 1 + np.argmax(fuzzy_levels)

    print('Level of understandability\t' + str(level))

