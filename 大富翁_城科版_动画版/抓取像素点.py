import pygame
import sys

pygame.init()

# 初始化屏幕
size = (1270, 768)
screen = pygame.display.set_mode(size)
pygame.display.set_caption("鼠标点击坐标")

running = True

backgroud = pygame.image.load("resource\\pic\\GameMap.png")
screen.blit(backgroud,(0,0))

while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.MOUSEBUTTONDOWN and event.button == 1:  # 1表示左键
            # 获取鼠标点击位置的坐标
            mouse_x, mouse_y = pygame.mouse.get_pos()
            print(f"鼠标点击坐标: ({mouse_x}, {mouse_y})")

    pygame.display.flip()

pygame.quit()
sys.exit()
