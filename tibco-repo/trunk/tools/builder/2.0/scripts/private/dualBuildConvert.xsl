<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" xalan:indent-amount="2" xmlns:xalan="http://xml.apache.org/xalan" />
    <xsl:param name="eclipseFeature"/>
    <xsl:template match="project">
        <xsl:copy>
            <taskdef resource="net/sf/antcontrib/antlib.xml" >
                <classpath>
                    <fileset includes="ant-contrib-1.0b3.jar">
                        <xsl:attribute name="dir"><xsl:text>${autobuild.dir.builder}</xsl:text></xsl:attribute>
                    </fileset>
                </classpath>
            </taskdef> 
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="property[@name='dual.build.enable']">
        <property name="eclipse.nature.build" value="true" />
        <var name="official-release-build" unset="true" /> 
        <var name="integration-build" unset="true" />
        <var name="dual.build.enable" unset="true" />
        <var name="assembly.packaging.enable" unset="true" />
    </xsl:template>
    <xsl:template match="property[@name='release-unit']">
        <var name="release-unit" >
            <xsl:attribute name="value"><xsl:value-of select="$eclipseFeature" /></xsl:attribute>
        </var>
    </xsl:template>
    <xsl:template match="property[@name='feature']">
        <var name="feature" >
            <xsl:attribute name="value"><xsl:value-of select="$eclipseFeature" /></xsl:attribute>
        </var>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
