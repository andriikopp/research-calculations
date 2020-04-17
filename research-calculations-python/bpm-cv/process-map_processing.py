import os
import cv2
import numpy as np


# input directory
maps_dir = 'process-map'
maps = os.listdir(maps_dir)

# templates directory and images
temp_dir = 'templates'
node_template = 'box_template.png'
arc_templates = ['arr_template_right.png', 'arr_template_left.png', 'arr_template_up.png', 'arr_template_down.png']

# threshold value to accept detected template
threshold = 0.85


# find unique objects that match template
def unique_template_matching(locations):
    unique_points = []

    for point in zip(*locations[::-1]):
        if len(unique_points) == 0:
            unique_points.append(point)
        else:
            unique = True

            for upt in unique_points:
                dist = np.sqrt((upt[0] - point[0]) ** 2 + (upt[1] - point[1]) ** 2)

                if dist <= threshold * 10:
                    unique = False
                    break

            if unique:
                unique_points.append(point)

    return unique_points


# process given images
for map_img in maps:
    print(map_img)
    img_name = maps_dir + '/' + map_img

    img_rgb = cv2.imread(img_name)
    img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)
    img_gray = cv2.GaussianBlur(img_gray, (3, 3), 0)

    # (i) detect nodes
    template = cv2.imread(temp_dir + '/' + node_template)
    template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
    template = cv2.GaussianBlur(template, (3, 3), 0)
    w, h = template.shape[::-1]

    res = cv2.matchTemplate(img_gray, template, cv2.TM_CCOEFF_NORMED)
    loc = np.where(res >= threshold)

    process_num = 0

    for pt in unique_template_matching(loc):
        cv2.rectangle(img_rgb, pt, (pt[0] + w, pt[1] + h), (0, 0, 255), 2)
        cv2.putText(img_rgb, 'node', (pt[0] + w, pt[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (0, 0, 255), 2)
        process_num += 1


    # (ii) detect arcs
    arr_num = 0

    for arc_template in arc_templates:
        template = cv2.imread(temp_dir + '/' + arc_template)
        template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
        template = cv2.GaussianBlur(template, (3, 3), 0)
        w, h = template.shape[::-1]

        res = cv2.matchTemplate(img_gray, template, cv2.TM_CCOEFF_NORMED)
        loc = np.where(res >= threshold)

        for pt in unique_template_matching(loc):
            cv2.rectangle(img_rgb, pt, (pt[0] + w, pt[1] + h), (255, 0, 0), 2)
            cv2.putText(img_rgb, 'arc', (pt[0] + w, pt[1] - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.4, (255, 0, 0), 2)
            arr_num += 1


    # (iii) calculate metrics
    print('N =', process_num)
    print('A =', arr_num)

    CNC_K = (process_num ** 2) / arr_num
    CNC_P = process_num / arr_num

    print('CNC_K =', CNC_K)
    print('CNC_P =', CNC_P)

    S = arr_num - process_num + 1

    print('S =', S)

    # add empty line after image data
    print()

    cv2.imshow('image', img_rgb)
    cv2.waitKey(0)

