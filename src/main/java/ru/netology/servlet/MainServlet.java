package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    final String GET = "GET";
    final String POST = "POST";
    final String DELETE = "DELETE";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET)) {
                methodGET(path, resp);
                return;
            }
            if (method.equals(POST)) {
                methodPOST(path, req, resp);
                return;
            }
            if (method.equals(DELETE)) {
                // easy way
                methodDELETE(path, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void methodGET(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/api/posts")) {
            controller.all(resp);
        } else if (path.matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")).replace("/", ""));
            controller.getById(id, resp);
        }
    }

    private void methodPOST(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (path.equals("/api/posts")) {
            controller.save(req.getReader(), resp);
        }
    }

    private void methodDELETE(String path, HttpServletResponse resp) {
        if (path.matches("/api/posts/\\d+")) {
            final var id = Long.parseLong(path.substring(path.lastIndexOf("/")).replace("/", ""));
            controller.removeById(id, resp);
        }
    }
}

