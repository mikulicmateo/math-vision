import cv2 as cv
import numpy as np

numbers_img = cv.imread('13540.jpg')
cv.imshow('numbers', numbers_img)

gray = cv.cvtColor(numbers_img, cv.COLOR_BGR2GRAY)
cv.imshow('gray numbers', gray)

blur = cv.medianBlur(gray, 5)
canny = cv.Canny(blur, 125, 250, cv.THRESH_BINARY)

cv.imshow('canny', canny)

rect_kernel = cv.getStructuringElement(cv.MORPH_RECT, (2, 2))
canny = cv.dilate(canny, rect_kernel, iterations=1)

contours, hierarchies = cv.findContours(canny, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
cv.drawContours(numbers_img, contours, -1, (255,0,0), 2)
cv.imshow('countured img', numbers_img)

contours_poly = [None] * len(contours)
bound_rect = [None] * len(contours)
for i, c in enumerate(contours):
    contours_poly[i] = cv.approxPolyDP(c, 2, True)
    bound_rect[i] = cv.boundingRect(contours_poly[i])

#bound_rect = [X Y W H]
#img[Y:Y+H, X:X+W] -> crop image
counter = 0
for rect in bound_rect:
    counter += 1
    color = 0,255,0
    cv.rectangle(numbers_img, (int(rect[0]), int(rect[1])), (int(rect[0]+rect[2]), int(rect[1]+rect[3])), color, 2)
    image_to_resize = canny[rect[1]:rect[1] + rect[3], rect[0]:rect[0]+rect[2]]

    #create black 100x100 image
    blank = np.zeros((100, 100), dtype='uint8')

    #rescale by 50% while y or x axis is greater than 100
    while (image_to_resize.shape[0] > 100) or (image_to_resize.shape[1] > 100):
        image_to_resize = cv.resize(image_to_resize, None, fx=0.5, fy=0.5, interpolation=cv.INTER_AREA)

    #calculate middle
    ax, ay = 50 - (image_to_resize.shape[1] // 2), 50 - (image_to_resize.shape[0] // 2)
    #write resized image to middle of the blank image
    blank[ay: ay + image_to_resize.shape[0], ax: ax + image_to_resize.shape[1]] = image_to_resize
    cv.imshow('win' + str(counter),  blank)
    cv.imwrite('images/'+str(i)+'.jpg', blank)


cv.imshow('draw', numbers_img)

cv.waitKey(0)