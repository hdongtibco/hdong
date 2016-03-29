<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan">
	<xsl:output method="text" indent="yes" xalan:indent-amount="2" />
    <xsl:param name="tibcoHome"/>

	<xsl:template match="/">
tibco.home=<xsl:value-of select="$tibcoHome"/>
plugins.home=<xsl:value-of select="fom/EnvConfiguration/pluginhome" />

# Environment Info

env.enable.ocv=<xsl:value-of select="fom/EnvConfiguration/enableocv" />
env.admin.hawk=<xsl:value-of select="fom/EnvConfiguration/adminhawk" />
#env.host.name=<xsl:value-of select="fom/EnvConfiguration/host" />
env.tra.home=<xsl:value-of select="fom/EnvConfiguration/trahome" />
fom.tibco.home=<xsl:value-of select="fom/CommonConfiguration/tibcofomhome" />
env.domain=<xsl:value-of select="fom/EnvConfiguration/domain" />
env.rv.daemon=<xsl:value-of select="fom/EnvConfiguration/rvdaemon" />
env.hawk.daemon=<xsl:value-of select="fom/EnvConfiguration/hawkdaemon" />
env.user=<xsl:value-of select="fom/EnvConfiguration/user" />
env.password=<xsl:value-of select="fom/EnvConfiguration/password" />
    </xsl:template>
</xsl:stylesheet>