<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
    <title>${currentLocation}</title>

    <link rel="stylesheet" href="${rc.getContextPath()}/css/ewp/website/rayootech/website.css" type="text/css" />

    <script src="${rc.getContextPath()}/js/jquery/jquery-1.6.4.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function(){
            $("li:has(ul)").hover(function(){
                $(this).children("ul").show();  
            },
            function(){
                $(this).children("ul").hide();
            })
        })
    </script>
</head>
