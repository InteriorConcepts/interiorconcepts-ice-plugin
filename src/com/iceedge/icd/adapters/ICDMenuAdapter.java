// 
// Decompiled by Procyon v0.5.36
// 

package com.iceedge.icd.adapters;

import com.iceedge.icd.actions.PrintTabConfigurationsAction;
import com.iceedge.icd.actions.ICDReportTestAction;
import com.iceedge.icd.actions.ToggleTabsAction;
import com.iceedge.icd.actions.ShowDXFExportAction;
import com.iceedge.icd.actions.ShowICDManufacturingReportAction;
import com.iceedge.icd.actions.ShowICDSheetAction;
import com.iceedge.icd.actions.ShowICDQuoteAction;
import com.iceedge.icd.actions.IsometricAssemblyElevation;
import com.iceedge.icd.actions.AllAssemblyElevationAction;
import com.iceedge.icd.actions.AssemblyElevationAction;
import com.iceedge.icd.actions.IsometricAssemblyElevation2DAction;
import com.iceedge.icd.actions.AllAssemblyElevation2DAction;
import com.iceedge.icd.actions.AssemblyElevation2DAction;
import com.iceedge.icd.actions.ShowTileInstallationTagsAction;
import com.iceedge.icd.actions.ShowFabricCuttingReportTagsAction;
import com.iceedge.icd.actions.ShowPreassembledTagAction;
import net.dirtt.menus.actions.ExportToSIFAction;
import java.util.ArrayList;
import net.dirtt.icecomponents.IceSubMenuJoinable;
import net.dirtt.icecomponents.IceSubMenu;
import java.util.List;
import net.iceedge.icecore.plugin.systemInterfaces.IceMenuAdapter;

public class ICDMenuAdapter implements IceMenuAdapter
{
    private static final String TO_ICD_SIF = "Export To ICD SIF";
    
    public List<IceSubMenu> buildRootMenus() {
        return null;
    }
    
    public List<IceSubMenuJoinable> buildFileMenu() {
        /*
        final ArrayList<ExportToSIFAction> list = (ArrayList<ExportToSIFAction>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new ExportToSIFAction(33, "Export To ICD SIF"));
        */
        return (List<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
    }
    
    public List<IceSubMenuJoinable> buildEditMenu() {
        return null;
    }
    
    public List<IceSubMenuJoinable> buildViewMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add(new ShowPreassembledTagAction());
        list.add(new ShowFabricCuttingReportTagsAction());
        list.add(new ShowTileInstallationTagsAction());
        return (List<IceSubMenuJoinable>)list;
    }
    
    public List<IceSubMenuJoinable> buildInsertMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new AssemblyElevation2DAction());
        list.add((IceSubMenuJoinable)new AllAssemblyElevation2DAction());
        list.add((IceSubMenuJoinable)new IsometricAssemblyElevation2DAction());
        return (List<IceSubMenuJoinable>)list;
    }
    
    public List<IceSubMenuJoinable> buildToolsMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new AssemblyElevationAction());
        list.add((IceSubMenuJoinable)new AllAssemblyElevationAction());
        list.add((IceSubMenuJoinable)new IsometricAssemblyElevation());
        return (List<IceSubMenuJoinable>)list;
    }
    
    public List<IceSubMenuJoinable> buildReportsMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new ShowICDQuoteAction());
        list.add((IceSubMenuJoinable)new ShowICDSheetAction());
        list.add((IceSubMenuJoinable)new ShowICDManufacturingReportAction());
        return (List<IceSubMenuJoinable>)list;
    }
    
    public List<IceSubMenuJoinable> buildSettingsMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new ShowDXFExportAction());
        list.add((IceSubMenuJoinable)new ToggleTabsAction());
        return (List<IceSubMenuJoinable>)list;
    }
    
    public List<IceSubMenuJoinable> buildExpertMenu() {
        return null;
    }
    
    public List<IceSubMenuJoinable> buildHelpMenu() {
        return null;
    }
    
    public List<IceSubMenuJoinable> buildDeveloperMenu() {
        final ArrayList<IceSubMenuJoinable> list = (ArrayList<IceSubMenuJoinable>)new ArrayList<IceSubMenuJoinable>();
        list.add((IceSubMenuJoinable)new ICDReportTestAction());
        list.add((IceSubMenuJoinable)new PrintTabConfigurationsAction());
        return (List<IceSubMenuJoinable>)list;
    }
}