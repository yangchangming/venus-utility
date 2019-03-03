<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="/">
   <div> 
      <xsl:for-each select="tool/group">
        <div><xsl:value-of select="id"/></div>
        <div><xsl:value-of select="name"/></div>
        <div><xsl:copy-of select="content/node()"/></div>
        <div><xsl:value-of select="operate"/></div>
      </xsl:for-each>      
  </div>
</xsl:template>
</xsl:stylesheet>