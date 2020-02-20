
![Image text](/static/shiro.png)

#### 1.主要功能
shiro主要有三大功能模块：
1. Subject：主体，一般指用户。
2. SecurityManager：安全管理器，管理所有Subject，可以配合内部安全组件。(类似于SpringMVC中的DispatcherServlet)
   SecurityManager 包含很多内置的模块来完成功能，比如登录（Authenticator），权限验证（Authorizer）等
3. Realms：用于进行权限信息的验证，一般需要自己实现。
#### 2 .细分功能
1. Authentication：身份认证/登录(账号密码验证)。
2. Authorization：授权，即角色或者权限验证。
3. Session Manager：会话管理，用户登录后的session相关管理。
4. Cryptography：加密，密码加密等。
5. Web Support：Web支持，集成Web环境。
6. Caching：缓存，用户信息、角色、权限等缓存到如redis等缓存中。
7. Concurrency：多线程并发验证，在一个线程中开启另一个线程，可以把权限自动传播过去。
8. Testing：测试支持；
9. Run As：允许一个用户假装为另一个用户（如果他们允许）的身份进行访问。
10. Remember Me：记住我，登录后，下次再来的话不用登录了。

#### 密码匹配
Realm在验证用户身份的时候，要进行密码匹配。最简单的情况就是明文直接匹配，然后就是加密匹配，
这里的匹配工作则就是交给CredentialsMatcher来完成的。

三个分支
1. AllowAllCredentialsMatcher：只要该用户名存在即可，不用去验证密码是否匹配。
2. PasswordMatcher：内部使用一个PasswordService 来完成匹配。从源码中，我们了解到了，对于服务器端存储的密码分成String和Hash两种，
   然后由PasswordService 来分别处理。所以PasswordMatcher 也只是完成了一个流程工作，具体的内容要到PasswordService 来看。
3. SimpleCredentialsMatcher：它的实现比较简单，就是直接比较AuthenticationToken的getCredentials() 
   和AuthenticationInfo 的getCredentials()内容，若为ByteSource则匹配下具体的内容，否则直接匹配引用。
   
login 过程

Subject.login() -> securityManager.login(this, token) -> AbstractAuthenticator/ModularRealmAuthenticator..authenticate(token)
 -> Realm .getAuthenticationInfo(token)

