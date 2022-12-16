package com.source;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Main {
    public static void main(String[] args) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        long window = GLFW.glfwCreateWindow(1920, 1080, "", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        IntBuffer width = MemoryStack.stackMallocInt(1);
        IntBuffer height = MemoryStack.stackMallocInt(1);
        IntBuffer channels = MemoryStack.stackMallocInt(1);

        ByteBuffer image = STBImage.stbi_load("/home/mainuser/Downloads/background.png", width, height, channels, 4);

        if (image == null) {
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
        }

        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0f, 0f);
            GL11.glVertex2f(0f, 0f);
            GL11.glTexCoord2f(0f, 1f);
            GL11.glVertex2f(0f, 1f);
            GL11.glTexCoord2f(1f, 0f);
            GL11.glVertex2f(1f, 0f);
            GL11.glTexCoord2f(1f, 1f);
            GL11.glVertex2f(1f, 1f);
            GL11.glEnd();

            // render here

            GLFW.glfwSwapBuffers(window);
        }
    }
}