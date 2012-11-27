<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signup {
        max-width: 300px;
        padding: 19px 29px 29px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signup .form-signup-heading,
      .form-signup .checkbox {
        margin-bottom: 10px;
      }
      .form-signup input[type="text"],
      .form-signup input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

</style>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<c:url value="/signup" var="signupUrl" />

<form class="form-signup" action="${signupUrl}" method="POST">
        <h2 class="form-signup-heading">Please sign up</h2>
        <input type="text" class="input-block-level" placeholder="First Name" name="firstName" value="${signupForm.firstName}">
        <input type="text" class="input-block-level" placeholder="Last Name" name="lastName" value="${signupForm.lastName}">
        <input type="text" class="input-block-level" placeholder="User Name" name="username" value="${signupForm.username}">
        <input type="password" class="input-block-level" placeholder="Password" name="password">
        <input type="text" class="input-block-level" placeholder="Comma Seperated list of Skills" name="skills">
        <textarea rows="5" class="input-block-level" placeholder="Address" name="address"></textarea>
        <button class="btn btn-large btn-primary" type="submit">Sign Up</button>
</form>