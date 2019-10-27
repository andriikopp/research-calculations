import cv2
import numpy as np

img_rgb = cv2.imread('diagram01.png')
img_gray = cv2.cvtColor(img_rgb, cv2.COLOR_BGR2GRAY)

templates = [
    ['and', 0, 'and.png', (255, 0, 0)],
    ['xor', 0, 'xor.png', (0, 255, 0)],
    ['or', 0, 'or.png', (0, 0, 255)]
]

threshold = 0.9

for path in templates:
    template = cv2.imread('templates/' + path[2])
    template = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
    w, h = template.shape[::-1]

    res = cv2.matchTemplate(img_gray, template, cv2.TM_CCOEFF_NORMED)
    loc = np.where(res >= threshold)

    unique_points = []

    for pt in zip(*loc[::-1]):
        if len(unique_points) == 0:
            unique_points.append(pt)
        else:
            unique = True

            for upt in unique_points:
                dist = np.sqrt((upt[0] - pt[0]) ** 2 + (upt[1] - pt[1]) ** 2)

                if dist <= threshold * 10:
                    unique = False
                    break

            if unique:
                unique_points.append(pt)

    print(path[0], unique_points)

    for pt in unique_points:
        cv2.rectangle(img_rgb, pt, (pt[0] + w, pt[1] + h), path[3], 2)
        path[1] += 1

cv2.imwrite('result.png',img_rgb)

print('Detected connectors:')

for path in templates:
    print(path[0], path[1])
