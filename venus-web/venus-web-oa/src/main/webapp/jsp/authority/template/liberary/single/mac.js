var xslt='<?xml version="1.0"?><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">';
      xslt+='<xsl:output method="html" omit-xml-declaration="yes"/><xsl:template match="root">';
      xslt+='<div id="page"><div id="menu"><xsl:for-each select="data">';
      xslt+='<a><xsl:attribute name="href"><xsl:text>javascript:forward(\'</xsl:text><xsl:value-of select="@url" />';
      xslt+='<xsl:text>\')</xsl:text></xsl:attribute><xsl:value-of select="@name"/></a></xsl:for-each>';
      xslt+='</div></div></xsl:template></xsl:stylesheet>';

jQuery(document).ready(function($){
  // Apply jqDock with no options...
  jQuery.getScript('./single/mac/jquery.jqDock.min.js', function() {
      $('#menu>a').each(function(index) {
        $(this).html('<img src="./single/mac/multi'+(index+1)+'.gif"/>');
      });
      
      var dockOptions ={ 
          align: 'bottom' // horizontal menu, with expansion UP/DOWN from centre
          , size: 50 //increase 'at rest' size to 60px
          , fadeIn: 1000 //fade in over 1 second          
      };
  	  $('#menu').jqDock(dockOptions);      
    });
});