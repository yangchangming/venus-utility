<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>

<xsl:template match="/">
    <xsl:apply-templates select="model"/>      
</xsl:template>
<xsl:template match="model">    
    <div id="tables"> 
    <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">database</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="@database"/></xsl:attribute>        
    </xsl:element>
      <xsl:for-each select="table">
        <table>
        <colgroup>
            <col span="*" width="100"/>
        </colgroup>
        <tbody>
        <tr>
            <td>表名</td><td colspan="6"><div id="tablename" class="modifytext"><xsl:value-of select="@tablename"/></div></td>
        </tr>
        <tr>
            <td>描述</td><td colspan="6"><div id="description" class="modifytext"><xsl:value-of select="@description"/></div></td>
        </tr>
        <tr>
                <td>列名</td>
                <td>编码</td>
                <td>类型</td>
                <td>是否主键</td>
                <td>是否外键</td>
                <td>允许null</td>
                <td>注释</td>
        </tr>
        <tr>
                <td>主键</td>
                <td>id</td>
                <td>CHAR(19)</td>
                <td>true</td>
                <td>true</td>
                <td>false</td>
                <td>主键</td>
        </tr>
        <xsl:for-each select="field">
               <xsl:if test="primarykey = 'false'">
                <tr>
                    <td><div id="name" class="modifytext"><xsl:value-of select="name"/></div></td>
                    <td><div id="code" class="modifytext"><xsl:value-of select="code"/></div></td>
                    <td><div id="type" class="modifyselect"><xsl:value-of select="type"/></div></td>
                    <td><div id="primarykey" class="modifyboolean"><xsl:value-of select="primarykey"/></div></td>
                    <td><div id="foreignkey" class="modifyboolean"><xsl:value-of select="foreignkey"/></div></td>
                    <td><div id="nullable" class="modifyboolean"><xsl:value-of select="nullable"/></div></td>
                    <td><div id="remark" class="modifytextarea"><xsl:value-of select="remark"/></div></td>
                </tr>
                </xsl:if>
            </xsl:for-each>
        </tbody>
        </table>
      </xsl:for-each>
  </div>
</xsl:template>
</xsl:stylesheet>