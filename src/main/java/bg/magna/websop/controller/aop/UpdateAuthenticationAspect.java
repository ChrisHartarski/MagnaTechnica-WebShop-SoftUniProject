package bg.magna.websop.controller.aop;

import bg.magna.websop.service.helper.UserHelperService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UpdateAuthenticationAspect {
    private final UserHelperService userHelperService;

    public UpdateAuthenticationAspect(UserHelperService userHelperService) {
        this.userHelperService = userHelperService;
    }

    @Pointcut("execution(* bg.magna.websop.service.impl.UserServiceImpl.editUserEmail(..))" +
            "|| execution(* bg.magna.websop.service.impl.UserServiceImpl.editUserPassword(..))")
    void onUpdateEmailOrPasswordPointcut() {}

    @After("onUpdateEmailOrPasswordPointcut()")
    public void afterUpdateEmailOrPassword(JoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();
        String userId = (String) arguments[1];

        userHelperService.updateAuthentication(userId);
    }
}
