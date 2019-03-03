$(document).ready(function(){
    
    $("ul#menu>li>ul>li>a").stop().animate({'marginLeft':'-85px'},1000);
    
    $('ul#menu>li>ul>li').hover(
                    function () {
                        $('a',$(this)).stop().animate({'marginLeft':'-2px'},200);
                    },
                    function () {
                        $('a',$(this)).stop().animate({'marginLeft':'-85px'},200);
                    }
                );    
});