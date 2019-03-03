<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="/">
   <div id="drag"> 
   <table>
        <colgroup>
            <col span="*" width="100"/>
        </colgroup>
        <tbody>
        <tr>
            <td><div class="alldrag"><image src="../../../images/ewp/move.jpg"></image></div></td>
            <td><div class="southdrag"><image src="../../../images/ewp/down.jpg"></image></div></td>
            <td><div class="southdrag"><image src="../../../images/ewp/down.jpg"></image></div></td>
            <td><div class="southdrag"><image src="../../../images/ewp/down.jpg"></image></div></td>
            <td><div class="southdrag"><image src="../../../images/ewp/down.jpg"></image></div></td>
        </tr>
      <xsl:for-each select="table/line">
        <tr>
            <td><div class="eastdrag"><image src="../../../images/ewp/right.jpg"></image></div></td>
            <td><div class="drag"><xsl:value-of select="name"/></div></td>
            <td><div class="drag"><xsl:value-of select="age"/></div></td>
            <td><div class="drag"><xsl:value-of select="gender"/></div></td>
            <td><div class="drag"><xsl:value-of select="occupation"/></div></td>
        </tr>
      </xsl:for-each> 
      </tbody>
  </table>
  </div>
</xsl:template>
</xsl:stylesheet>