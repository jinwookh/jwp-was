package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.view.RedirectView;
import http.response.view.View;
import model.AuthorizationFailException;
import model.User;
import service.UserService;
import session.HttpSession;
import session.InMemoryHttpSessionContainer;
import utils.QueryStringUtils;

import java.util.Map;

public class UserLoginController extends AbstractController {
    private static final String LOGIN_USER = "login-user";
    private static final String EVERYWHERE = "/";

    private UserService userService = new UserService();

    @Override
    View doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> body = QueryStringUtils.parse(request.getBody());

        try {
            User foundUser = userService.login(body.get("userId"), body.get("password"));

            HttpSession httpSession = request.getSession(InMemoryHttpSessionContainer.getInstance());
            if (httpSession == null) {
                httpSession = InMemoryHttpSessionContainer.getInstance().createSession();
            }

            response.addSession(httpSession);

            httpSession.setAttribute(LOGIN_USER, foundUser);
            return new RedirectView("/index.html");
        } catch (AuthorizationFailException e) {
            return new RedirectView("/user/login_failed.html");
        }
    }
}
