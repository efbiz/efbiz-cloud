<!DOCTYPE html>

<html lang="zh">
<head>
  <meta content="charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="keywords" content="配件，奔驰，宝马，奥迪，路虎，沃尔沃，保时捷，捷豹，进口大众，全车件，高端车，汽配，零配件，开思，开思汽配交易平台，开思汽车配件网，开思时代官网，深圳开思时代科技有限公司，汽修ERP，汽修ERP云" />
  <meta name="description" content="开思电商，专注于奔驰、宝马、奥迪、路虎、沃尔沃、保时捷、捷豹等高端车配件、特别是全车件采购。拥有超级EPC核心技术，实现VIN锁唯一配件；精选源头供应商，提供优质供给；提供正品保障、假一赔十和一年质保等保障条款.....让采购更放心" />
  <title>开思汽配-优质供给、正品保障、假一赔十、一年质保。专注奔驰、宝马、奥迪、路虎、沃尔沃、保时捷、捷豹等高端车配件，专注全车件</title>
  <link rel="shortcut icon" href="${commonStatic}/common/favicon.ico" />
  <link rel="stylesheet" href="${commonStatic}/common/bootstrap-3.3.4-dist/css/bootstrap.min.css" type="text/css"/>
  <link rel="stylesheet" href="${commonStatic}/common/reset.css" type="text/css"/>
  <link rel="stylesheet" href="${commonStatic}/common/base.css" type="text/css"/>
  <link rel="stylesheet" href="${request.contextPath}/css/login.css" type="text/css"/>
  <link rel="stylesheet" href="${request.contextPath}/css/nivo-slider.css" type="text/css"/>
  <style type="text/css">
    .login-container .login-form > li.message { color: red; }
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
			<#if RequestParameters['error']??>
			<li class="message text-left" style="color:#E22E33">登入失败，用户名或密码错误！</li>
			</#if>
			<li class="input">
			    <input id="userName" name="username" tabindex="1" placeholder="用户名/手机号" type="text" value="" size="25"/>
			    <i class="i-username"></i>
			</li>
			<li class="input">
				<input id="password" name="password" tabindex="2" placeholder="密码" type="password" value="" size="25" autocomplete="off"/>
				<i class="i-password"></i>
			</li>
			<li class="auto-login-li">
				<ul class="fix">
				    <li class="auto-login fl" onclick="check()">
						<b id="rmbPassword" class="active"></b>
		           	    <a href="javascript:void(0)" style="color:#666666;">记住我</a>
					</li>
					<li class="look-for-pwd fr">
		           	    <a href="javascript:void(0)" onclick="lookForPassword()" style="color:#666666;">找回密码?</a>
					</li>
				</ul>
			</li>
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
       <a href="#">首 &nbsp;页</a>
       <span>|</span>
       <a href="http://www.casstime.com/#team" target="_blank">关于我们</a>
       <span>|</span>
       <a href="http://www.casstime.com/#join" target="_blank">联系我们</a>
       <span>|</span>
       <a href="#">商家入驻</a>
       <span>|</span>
       <a href="http://www.casstime.com/#join" target="_blank">人才招聘</a>
       <span>|</span>
       <a href="#">友情链接</a>
       <span>|</span>
       <a href="#">服务网点</a>
       <span>|</span>
       <a class="weixin">官方微信</a>
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