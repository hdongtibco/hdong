<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/messaging/pluginhome" />

# Environment Info
#mess.host.name=<xsl:value-of select="fom/messaging/host" />
#mess.port=<xsl:value-of select="fom/messaging/port" />
mess.ems.servers=<xsl:value-of select="fom/messaging/emsurls" />
mess.ems.user=<xsl:value-of select="fom/messaging/user" />
mess.ems.password=<xsl:value-of select="fom/messaging/password" />
mess.ems.home=<xsl:value-of select="fom/messaging/emshome" />
fom.tibco.home=<xsl:value-of select="fom/CommonConfiguration/tibcofomhome" />
    </xsl:template>
</xsl:stylesheet>