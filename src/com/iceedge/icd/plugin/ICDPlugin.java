// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.plugin;

import net.iceedge.icecore.plugin.annotation.IcePluginInit;
import com.iceedge.icebox.icecore.system.settings.plugin.IceFileSettingsAdapter;
import com.iceedge.icd.adapters.ICDIceFileSettingsAdapter;
import net.iceedge.icecore.plugin.systemInterfaces.IceMenuAdapter;
import com.iceedge.icd.adapters.ICDMenuAdapter;
import net.iceedge.icecore.plugin.systemInterfaces.IceTestsAdapter;
import com.iceedge.icd.adapters.ICDTestAdapter;
import net.iceedge.icecore.plugin.systemInterfaces.IceVersionAdapter;
import com.iceedge.icd.adapters.ICDVersionControlAdapter;
import net.iceedge.icecore.plugin.systemInterfaces.IceQuoteAdapter;
import com.iceedge.icd.adapters.ICDQuoteAdapter;
import com.iceedge.icd.reporting.ICDQuoteReportType;
import com.iceedge.icd.reporting.ICDFabricCutListReportType;
import com.iceedge.icd.reporting.ICDManufacturingReportType;
import net.dirtt.icelib.report.ReportType;
import org.apache.log4j.Logger;
import net.iceedge.icecore.plugin.module.MenusCustomizer;
import com.google.inject.Inject;
import net.iceedge.icecore.plugin.module.IceBoxMainPanelCustomizer;
import net.iceedge.icecore.plugin.annotation.IcePlugin;

@IcePlugin(name = "Interior Concepts", provider = "Ice Edge Business Solutions", version = "1.0.0.0", apiVersion = "1.0.0.0", displayProperty = "display.ICD")
public class ICDPlugin
{
    @Inject
    private IceBoxMainPanelCustomizer iceBoxMainPanelCustomizer;
    @Inject
    private MenusCustomizer iceMenus;
    private static final Logger LOG;
    
    @IcePluginInit
    void initialise() {
        this.iceBoxMainPanelCustomizer.registerIceQuoteAdapter((IceQuoteAdapter)new ICDQuoteAdapter(new ReportType[] { new ICDManufacturingReportType(), new ICDFabricCutListReportType(), new ICDQuoteReportType() }));
        this.iceBoxMainPanelCustomizer.registerIceVersionAdapter((IceVersionAdapter)new ICDVersionControlAdapter());
        this.iceBoxMainPanelCustomizer.registerIceTestsAdapter((IceTestsAdapter)new ICDTestAdapter());
        this.iceMenus.registerIceMenuAdapter((IceMenuAdapter)new ICDMenuAdapter());
        this.iceMenus.registerIceFileSettingsAdapter((IceFileSettingsAdapter)new ICDIceFileSettingsAdapter());
        ICDPlugin.LOG.info((Object)"ICD Plugin initialization complete!");
    }
    
    static {
        LOG = Logger.getLogger((Class)ICDPlugin.class);
    }
}
