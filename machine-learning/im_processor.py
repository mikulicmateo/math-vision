import cv2 as cv
import numpy as np


def preprocess_img(img):
    # numbers_img = cv.imread(img)
    numbers_img = np.ascontiguousarray(img, dtype=np.uint8)
    #gray = cv.cvtColor(numbers_img, cv.COLOR_BGR2GRAY)
    blur = cv.medianBlur(numbers_img, 5)
    canny = cv.Canny(blur, 125, 250, cv.THRESH_BINARY)
    rect_kernel = cv.getStructuringElement(cv.MORPH_RECT, (2, 2))
    canny = cv.dilate(canny, rect_kernel, iterations=1)
    contours, hierarchies = cv.findContours(canny, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    cv.drawContours(numbers_img, contours, -1, (255, 0, 0), 2)

    contours_poly = [None] * len(contours)
    bound_rect = [None] * len(contours)
    for i, c in enumerate(contours):
        contours_poly[i] = cv.approxPolyDP(c, 2, True)
        bound_rect[i] = cv.boundingRect(contours_poly[i])

    if len(bound_rect) < 1:
        return None
    return_val = resize_img(canny, bound_rect[0])
    return return_val


def resize_img(img, rect):
    image_to_resize = img[rect[1]:rect[1] + rect[3], rect[0]:rect[0] + rect[2]]

    # create black 100x100 image
    blank = np.zeros((100, 100, 1), dtype='uint8')

    # rescale by 50% while y or x axis is greater than 100
    while (image_to_resize.shape[0] > 100) or (image_to_resize.shape[1] > 100):
        image_to_resize = cv.resize(image_to_resize, None, fx=0.5, fy=0.5, interpolation=cv.INTER_AREA)

    # calculate middle
    ax, ay = 50 - (image_to_resize.shape[1] // 2), 50 - (image_to_resize.shape[0] // 2)
    # write resized image to middle of the blank image
    blank[ay: ay + image_to_resize.shape[0], ax: ax + image_to_resize.shape[1], 0] = image_to_resize
    cv.imwrite('images/' + 'try' + '.jpg', blank)

    return blank
