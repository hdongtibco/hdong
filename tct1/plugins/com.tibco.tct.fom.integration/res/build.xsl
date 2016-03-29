<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/integration/pluginhome" />

# Integration Info
fom.tibco.home=<xsl:value-of select="fom/CommonConfiguration/tibcofomhome" />
int.host.name=<xsl:value-of select="fom/integration/hostname" />
int.port=<xsl:value-of select="fom/integration/port" />
int.node=<xsl:value-of select="fom/integration/node" />
int.offline.dir=<xsl:value-of select="fom/integration/offlinedir" />
int.server.port=<xsl:value-of select="fom/integration/serverport" />
int.server.host=<xsl:value-of select="fom/integration/serverhost" />
int.enterprise=<xsl:value-of select="fom/integration/enterprise" />
int.user=<xsl:value-of select="fom/integration/username" />
int.password=<xsl:value-of select="fom/integration/password" />
int.fp.config.enable=<xsl:value-of select="fom/integration/enableFPConfig" />
int.owner.fp=<xsl:value-of select="fom/integration/ownerFP" />

<xsl:choose>
<xsl:when test="fom/integration/isoffline='true'">
int.isoffline=true
int.isonline=false
</xsl:when>
<xsl:otherwise>
int.isoffline=false
int.isonline=true
</xsl:otherwise>
</xsl:choose>

    </xsl:template>
</xsl:stylesheet>