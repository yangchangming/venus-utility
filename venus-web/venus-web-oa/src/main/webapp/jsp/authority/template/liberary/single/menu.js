$(document).ready(function(){
    
    //Fix Errors - http://www.learningjquery.com/2009/01/quick-tip-prevent-animation-queue-buildup/
    
	//Remove outline from links
    $("ul#menu>li>ul>li>a").click(function(){
        $(this).blur();
    });
    
    //When mouse rolls over
    $("ul#menu>li>ul>li").mouseover(function(){
        $(this).stop().animate({height:'135px'},{queue:false, duration:600, easing: 'easeOutBounce'})
    });
    
    //When mouse is removed
    $("ul#menu>li>ul>li").mouseout(function(){
        $(this).stop().animate({height:'40px'},{queue:false, duration:600, easing: 'easeOutBounce'})
    });
    
});