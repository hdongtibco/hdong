<?xml version='1.0' encoding='utf-8' ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
<xsl:output method="text" media-type="text/plain"/>
    <xsl:param name="propname" select="'_release_units_list'" />
    <xsl:template match="/"><xsl:value-of select="$propname" /><xsl:text>=</xsl:text>
        <xsl:apply-templates select="featureOrder/release-unit" />
        <xsl:call-template name="finish" /></xsl:template>
    <xsl:template match="featureOrder/release-unit">
        <xsl:variable name="location">
            <xsl:call-template name="escape-backslash">
                <xsl:with-param name="string-in" select="@location" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$location" /><xsl:text>, </xsl:text></xsl:template>
    <xsl:template name="finish"><xsl:text>
</xsl:text></xsl:template>
    <xsl:template name="escape-backslash">
        <xsl:param name="string-in" />
        <xsl:variable name="backslash" select="'\'" />
        <xsl:variable name="escaped-backslash" select="'\\'" />
        <xsl:choose>
            <xsl:when test="contains($string-in,$backslash)">
                <xsl:value-of select="concat(substring-before($string-in,$backslash),$escaped-backslash)"/>
                <xsl:call-template name="escape-backslash">
                    <xsl:with-param name="string-in" select="substring-after($string-in,$backslash)"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string-in"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>