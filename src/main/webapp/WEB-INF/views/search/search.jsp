<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="resources/js/jquery.tmpl.min.js"></script>
<style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-search {
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
      .form-search .form-search-heading,
      .form-search .checkbox {
        margin-bottom: 10px;
      }
      .form-search input[type="text"],
      .form-search input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

</style>

<c:if test="${not empty message}">
<div class="${message.type.cssClass}">${message.text}</div>
</c:if>

<c:url value="/search" var="searchUrl" />

<form class="form-search" action="${searchUrl}" method="POST">
        <h2 class="form-search-heading">Search Jobs</h2>
        <input type="text" class="input-block-level" placeholder="Comma Seperated List of Skills" name="skills" id="skills">
         <textarea rows="5" class="input-block-level" placeholder="Address" name="address" id="address"></textarea>
         <p class="lead">choose one of address or latitude and longitude</p>
        <input type="text" class="input-block-level" placeholder="Latitude" name="latitude" id="latitude">
        <input type="text" class="input-block-level" placeholder="Longitude" name="longitude" id="longitude">
        <input type="text" class="input-block-level" placeholder="Within Distance (in kms)" name="withinDistance" id="withinDistance">
       
        <button class="btn btn-large btn-primary" type="submit" id="searchJobs">Search Jobs</button>
</form>

<div id="results">

</div>

<script type="text/javascript"> 

	$('#searchJobs').click(function(){
	    return getResult();
	});
	
    function getResult()
    {
    	var skills =  $("#skills").val();  
    	var address = $("#address").val();
    	var latitude= $("#latitude").val();
    	var longitude = $("#longitude").val();
    	var withinDistance = $("#withinDistance").val();

    	if(address){
    		$.get("resources/templates/jobs.html", function (template){
                $.get("jobs/near/"+address+"/"+skills  , function (data){ 
                    $("#results").html("<h3>We have found following Jobs for you..</h3>");
                    $.tmpl(template, data).appendTo("#results"); 
                });  

            }); 
    	}else if(latitude && longitude  && skills){
    		$.get("resources/templates/jobs.html", function (template){
                //alert(template);
                $.get("jobs/near/"+skills+"/?latitude="+latitude+"&longitude="+longitude , function (data){ 
                    $("#results").html("<h3>We have found following Jobs for you..</h3>");
                    $.tmpl(template, data).appendTo("#results"); 
                });  

            }); 
    	}else if(latitude && longitude){
    		$.get("resources/templates/jobs.html", function (template){
                //alert(template);
                $.get("jobs/near/"+"?latitude="+latitude+"&longitude="+longitude , function (data){ 
                    $("#results").html("<h3>We have found following Jobs for you..</h3>");
                    $.tmpl(template, data).appendTo("#results"); 
                });  

            }); 
    	}
        
        
        return false;
    }
    
    function getDetails(code)
    {
    	alert("***" + code);
        $.get("resources/template/jobdetails.html", function (template){
            $.get("/job/details/" + code, function (data){ 
                $("#dataRegion").html("");
                $.tmpl(template, data).appendTo("#dataRegion"); 
            });
        }); 
        return false;
    }

    getResult();

 </script>
