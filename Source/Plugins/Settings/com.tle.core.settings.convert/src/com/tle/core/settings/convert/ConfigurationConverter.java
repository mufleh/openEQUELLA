/*
 * Created on 4/05/2006
 */
package com.tle.core.settings.convert;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.tle.beans.Institution;
import com.tle.common.filesystem.handle.TemporaryFileHandle;
import com.tle.core.guice.Bind;
import com.tle.core.institution.convert.AbstractConverter;
import com.tle.core.institution.convert.ConverterParams;
import com.tle.core.institution.convert.PostReadMigrator;
import com.tle.core.plugins.PluginService;
import com.tle.core.plugins.PluginTracker;
import com.tle.core.settings.convert.extension.ConfigurationConverterExtension;
import com.tle.core.settings.service.ConfigurationService;

@Bind
@Singleton
public class ConfigurationConverter extends AbstractConverter<Map<String, String>>
{
	public static final String PROPERTIES_FILE = "properties/properties.xml"; //$NON-NLS-1$

	@Inject
	private ConfigurationService configurationService;
	// Guice plugin tracker modules can't handle '?'
	private PluginTracker<ConfigurationConverterExtension<?>> extensions;

	@Override
	public void doDelete(Institution institution, ConverterParams params)
	{
		configurationService.deleteAllInstitutionProperties();
	}

	@Override
	public void doExport(TemporaryFileHandle staging, Institution institution, ConverterParams callback)
		throws IOException
	{
		Map<String, String> allProperties = configurationService.getAllProperties();
		xmlHelper.writeXmlFile(staging, PROPERTIES_FILE, allProperties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doImport(TemporaryFileHandle staging, Institution institution, final ConverterParams params)
		throws IOException
	{
		if( !fileSystemService.fileExists(staging, PROPERTIES_FILE) )
		{
			return;
		}

		Map<String, String> allProperties = (Map<String, String>) xmlHelper.readXmlFile(staging, PROPERTIES_FILE);
		Collection<PostReadMigrator<Map<String, String>>> migrations = getMigrations(params);
		runMigrations(migrations, allProperties);
		configurationService.importInstitutionProperties(allProperties);
		for( ConfigurationConverterExtension<?> stub : extensions.getBeanList() )
		{
			stub.run(params.getOld2new());
		}
	}

	@Override
	public ConverterId getConverterId()
	{
		return ConverterId.CONFIGURATION;
	}

	@Inject
	public void setPluginService2(PluginService pluginService)
	{
		extensions = new PluginTracker<ConfigurationConverterExtension<?>>(pluginService, getClass(),
			"configurationConverterExtension", null);
	}
}