<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
<xsl:output method="text" media-type="text/plain"/>
    <xsl:template match="/">
        <xsl:for-each select="properties/target">
            <xsl:variable name="name" select="property/@name" />
            <xsl:if test="$name = 'svn:externals'" >
path=<xsl:value-of select="@path" />
            </xsl:if>  
        </xsl:for-each>  
    </xsl:template> 
</xsl:stylesheet>
