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
    # open source process diagram, make it gray, and blur it
    img_rgb = cv2.imread(input_dir + '/' + diagram)
    img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)
    img_gray = cv2.GaussianBlur(img_gray, (3, 3), 0)

    # templates for matching
    # each has name, number of occurrence, path, color
    templates = [
        # nodes templates
        ['xor', 0, 'xor.png'],
        ['or', 0, 'or.png'],
        ['and', 0, 'and.png'],
        ['function', 0, 'function.png'],
        ['event', 0, 'event.png'],

        # arcs templates
        ['arc-down', 0, 'arc-down.png'],
        ['arc-up', 0, 'arc-up.png'],
        ['arc-right', 0, 'arc-right.png'],
        ['arc-left', 0, 'arc-left.png']
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

    # get values of certain metric
    xor_conns = templates[0][1]
    or_conns = templates[1][1]
    and_conns = templates[2][1]
    functions = templates[3][1]
    events = templates[4][1]
    arcs = templates[5][1] + templates[6][1] + templates[7][1] + templates[8][1]

    # total number of nodes
    nodes = functions + events + xor_conns + or_conns + and_conns

    # total number of connectors
    connectors = xor_conns + or_conns + and_conns

    # thresholds of metrics
    metrics_thresholds = [48, 8, 22, 40, 62]

    # calculated metrics
    metrics = [nodes, connectors, events, functions, arcs]

    # fuzzy value of metrics
    fuzzy_metrics = []

    # calculate fuzzy values of metrics
    for i in range(0, len(metrics)):
        fuzzy_value = 0

        if metrics[i] <= metrics_thresholds[i]:
            fuzzy_value = metrics[i] / metrics_thresholds[i]
        else:
            fuzzy_value = 1

        fuzzy_metrics.append(fuzzy_value)

    # define is process model error-prone
    has_errors = np.max(fuzzy_metrics)

    # print diagram name and corresponding metrics
    print('{}\t{}\t{}\t{}\t{}\t{}\t{}'.format(diagram, nodes, connectors, events, functions, arcs,
                                              has_errors))

