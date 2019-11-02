/**
 * <p> global toggle operation </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-11-02 11:57
 */

/**
 * 切换左侧导航栏
 * @param source
 */
function doToggle(source){
    //侧边栏工具按钮伸展和收缩
    if ($('.toggle-area').hasClass('expand')){
        $('.toggle-area').removeClass('expand');
        $('.toggle-area').addClass('narrow');
    }else if ($('.toggle-area').hasClass('narrow')){
        $('.toggle-area').removeClass('narrow');
        $('.toggle-area').addClass('expand');
    }
    if ($('.fa-nav-toggle').hasClass('expand')){
        $('.fa-nav-toggle').removeClass('expand');
        $('.fa-nav-toggle').addClass('narrow');
        $('.fa-nav-toggle > i').removeClass("icon-circle-arrow-left");
        $('.fa-nav-toggle > i').addClass("icon-circle-arrow-right");
    }else if($('.fa-nav-toggle').hasClass('narrow')){
        $('.fa-nav-toggle').removeClass('narrow');
        $('.fa-nav-toggle').addClass('expand');
        $('.fa-nav-toggle > i').removeClass("icon-circle-arrow-right");
        $('.fa-nav-toggle > i').addClass("icon-circle-arrow-left");
    }

    //侧边栏伸展和收缩
    if ($('#sidebar > ul').hasClass('expand')) {
        $('#sidebar > ul').find('span').each(function (e) {
            $(this).css("display","none");
        });

        $('#sidebar > ul').removeClass('expand');
        $('#sidebar > ul').addClass('narrow');
    }else if ($('#sidebar > ul').hasClass('narrow')){
        $('#sidebar > ul').find('span').each(function (e) {
            $(this).css("display","inline");
        });

        $('#sidebar > ul').removeClass('narrow');
        $('#sidebar > ul').addClass('expand');
    }

    //内容主显示区伸展和收缩
    if ($('#content').hasClass('expand')){
        $('#content').removeClass('expand');
        $('#content').addClass('narrow');
    }else if ($('#content').hasClass('narrow')){
        $('#content').removeClass('narrow');
        $('#content').addClass('expand');
    }


}

