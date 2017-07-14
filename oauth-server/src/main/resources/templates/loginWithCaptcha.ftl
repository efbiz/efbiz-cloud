<!DOCTYPE html>

<html lang="zh">
<head>
  <meta content="charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>&#30331;&#24405;- &#24320;&#24605;&#27773;&#37197;</title>
  <link rel="shortcut icon" href="${commonStatic}/common/favicon.ico" />
  <link rel="stylesheet" href="${commonStatic}/common/bootstrap-3.3.4-dist/css/bootstrap.min.css" type="text/css"/>
  <link rel="stylesheet" href="${commonStatic}/common/reset.css" type="text/css"/>
  <link rel="stylesheet" href="${commonStatic}/common/base.css" type="text/css"/>
  <link rel="stylesheet" href="${request.contextPath}/css/login.css" type="text/css"/>
  <link rel="stylesheet" href="${request.contextPath}/css/nivo-slider.css" type="text/css"/>
  <style type="text/css">
    .login-container .login-form .title { height:60px; padding: 20px 0; }
    .login-container .login-form .input { height: 60px; }
    .login-container .login-form > li.message { padding-top: 1px; margin-top: -10px; color: red; }
  </style>
</head>
<body>
<header>
<div class="login-top container">
	<a href="#" title="开思汽配" class="fl"><img style="margin-top: 7px;" alt="开思汽配" src="${request.contextPath}/images/logo.png"></a>
	<div class="login-top-hotline">
	<i class="icon hotline-icon"></i>
	<span>客服热线：400-168-6600</span>
	</div>
</div>
<div class="login-top-split">
</div>
</header>
<div class="login-container container-fluid text-center">

<div class="container">
	<!-- <div class="banner-container"><img src="${request.contextPath}/images/banner1.jpg" style="width:788px; height:390px;"></div> -->
	<div class="slider-wrapper">  
	    <div id="slider" class="nivoSlider">  
	        <img src="${request.contextPath}/images/banner3.jpg" data-thumb="${request.contextPath}/images/banner3.jpg" alt="" />    
	        <img src="${request.contextPath}/images/banner1.jpg" data-thumb="${request.contextPath}/images/banner1.jpg" alt="" />  
	    </div>
	</div>
	<div class="login-body">
		<form id="fm1" class="fm-v clearfix" action="login" method="post" onsubmit="login();">
		<ul class="login-form">
			<li class="title">
				<h3 class="fl">会员登录</h3>
				<div class="to-register-div">还没有开思账号？<a class="register-a" target="_self" href="/passport/register">立即注册</a></div>
			</li>
			<li class="input">
			    <input id="userName" name="username" tabindex="1" placeholder="用户名" type="text" value="" size="25"/>
			    <i class="i-username"></i>
			</li>
			<li class="input">
				<input id="password" name="password" tabindex="2" placeholder="密码" type="password" value="" size="25" autocomplete="off"/>
				<i class="i-password"></i>
			</li>
			<li class="input text-left">
				<input id="captcha" name="captcha" tabindex="3" type="text" value="" style="float:left;width:140px;padding-left:8px;"/>
				<div style="float:left;margin-left:4px;"><img src="captcha-image" style="width: 100px;height: 36px;cursor:pointer;margin-left: 20px;margin-top: 2px;" id="codeImg" alt="看不清？换一张" title="看不清？换一张" onclick="this.src='captcha-image?' + Math.random();" /></div>
				<div style="float:right;margin-left:4px;height:40px;padding-top:14px;"><a href="javascript:void(0)" onclick="lookForPassword()" style="color:#666666;">忘记密码</a></div>
			</li>
			<!-- <li class="auto-login-li">
				<ul class="fix">
					<li class="look-for-pwd fr">
		           	    <a href="javascript:void(0)" onclick="lookForPassword()" style="color:#666666;">找回密码?</a>
					</li>
				</ul>
			</li> -->
			<#if RequestParameters['error']??>
			<li class="message text-left" >
				<#if RequestParameters['error']=='e2'>
					验证码错误！
				<#else>
					登入失败，用户名或密码错误！
				</#if>
			</li>
			</#if>
			<li class="fix">
				<input class="btn-submit" accesskey="l" value="登录" tabindex="4" type="submit"/>
			</li>
		</ul>
		</form>
	</div>
</div>
<footer>
<div class="container-fluid text-center" style="margin-top:20px;">
	<div>
       <a href="/portal/main">首 &nbsp;页</a>
       <span>|</span>
       <a href="/portal/main">关于我们</a>
       <span>|</span>
       <a href="/portal/main">联系我们</a>
       <span>|</span>
       <a href="/portal/main">商家入驻</a>
       <span>|</span>
       <a href="/portal/main">人才招聘</a>
       <span>|</span>
       <a href="/portal/main">友情链接</a>
       <span>|</span>
       <a href="/portal/main">服务网点</a>
       <span>|</span>
       <a href="/portal/main">官方微信</a>
    </div>
    <div class="copy-right">
    	Copyright @ 2017 CassTime. <a href="http://www.miitbeian.gov.cn" target="_blank">ICP证:粤ICP备15084413号-1</a>
    </div>
</div>
</footer>

</body>
<script type="text/javascript" src="${commonStatic}/common/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/login.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery.nivo.slider.pack.js"></script>
<script type="text/javascript">  
$(window).load(function() {  
    $('#slider').nivoSlider({
        directionNav:false,
    });  
});  
</script>
</html>