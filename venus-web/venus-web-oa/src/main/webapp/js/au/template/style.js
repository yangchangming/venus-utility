jQuery(document).ready(function(){
	//H
	var slider = '<div class="slider"><div class="handle"></div></div>';
	var image = '<ul>';
    image += '<li class="lih"><img class="imgh" src="../liberary/single/colonnade.gif" align="top"/></li> ';
    image += '<li class="lih"><img class="imgh" src="../liberary/single/mac.gif" align="top"/></li>';
    image += '<li class="lih"><img class="imgh" src="../liberary/single/menu.gif" align="top"/></li>';
    image += '<li class="lih"><img class="imgh" src="../liberary/single/navigation.gif" align="top"/></li>';
    image += '<li class="lih"><img class="imgh" src="../liberary/table/table.gif" align="top"/></li>';
    image += '<li class="lih"><img class="imgh" src="../liberary/tree/tree.gif" align="top"/></li>';
    image += '</ul>';
	jQuery(image).appendTo('.sliderGallery');
	jQuery(slider).appendTo('.sliderGallery');
	
	jQuery( ".imgh" ).aeImageResize({ height: 200, width: 200 });
	
	jQuery( ".imgh" ).hover(function(){
		jQuery(".imgh").css({border:''});
		jQuery(this).css({border:'3px solid #3399CC'});
	});
	jQuery( ".imgh" ).draggable({helper:'clone',cursor: 'move',stop: function (ev, ui){
        var pos = jQuery(ui.helper).offset();
        //alert(pos.left+'-'+pos.top);
        for(var i in layouts){
            var l = layouts[i];
            if(pos.left>=l.x&&pos.left<=(l.x+l.w)&&pos.top>=l.y&&pos.top<=(l.y+l.h)){
            	alert(jQuery(ui));
            }else{
            	
            }
        }
    }});
	
	var container = jQuery('div.sliderGallery');
    var ul = jQuery('ul', container);
    
    var itemsWidth = ul.innerWidth() - container.outerWidth();
    jQuery('.handle').slider({
        min: 0+1,
        max: itemsWidth-1,
        stop: function (event, ui) {
            ul.animate({'left' : ui.value * -1}, 500);
        },
        slide: function (event, ui) {
            ul.css('left', ui.value * -1);
        }
    });
    
    //V
    var sliderV = '<div class="sliderV"><div class="handleV"></div></div>';
    var image = '<ul>';
    image += '<li class="liv"><img class="imgv" src="../liberary/single/colonnade.gif" style="margin-bottom:20px" align="left"/></li> ';
    image += '<li class="liv"><img class="imgv" src="../liberary/single/mac.gif" style="margin-bottom:20px" align="left"/></li>';
    image += '<li class="liv"><img class="imgv" src="../liberary/single/menu.gif" style="margin-bottom:20px" align="left"/></li>';
    image += '<li class="liv"><img class="imgv" src="../liberary/single/navigation.gif" style="margin-bottom:20px" align="left"/></li>';
    image += '<li class="liv"><img class="imgv" src="../liberary/table/table.gif" style="margin-bottom:20px" align="left"/></li>';
    image += '<li class="liv"><img class="imgv" src="../liberary/tree/tree.gif" style="margin-bottom:20px" align="left"/></li>';
    image += '</ul>';
    jQuery(sliderV).appendTo('.sliderGalleryV');
    jQuery(image).appendTo('.sliderGalleryV'); 
    
    jQuery( ".imgv" ).aeImageResize({ height: 200, width: 170 });
    
    jQuery( ".imgv" ).hover(function(){
        jQuery(".imgv").css({border:''});
        jQuery(this).css({border:'3px solid #3399CC'});
    });
    jQuery( ".imgv" ).draggable({helper:'clone',cursor: 'move'});
       
    var containerV = jQuery('div.sliderGalleryV');
    var ulV = jQuery('ul', containerV);
    
    var itemsHeight = ulV.innerHeight() - containerV.outerHeight();
    jQuery('.handleV').slider({
    	orientation: "vertical",
        min: 0+1,
        max: itemsHeight-1,
        stop: function (event, ui) {
            ulV.animate({'top' : ui.value * -1}, 500);
        },
        slide: function (event, ui) {
            ulV.css('top', ui.value * -1);
        }
    });
        
});