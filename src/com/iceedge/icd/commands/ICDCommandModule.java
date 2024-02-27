package com.iceedge.icd.commands;

import net.dirtt.icelib.report.generalquote.icd.ICDQuote;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import net.dirtt.utilities.IceFileUtilities;
import net.dirtt.icelib.filehandling.FileFilters.TXTFileFilter;
import java.awt.Component;
import net.dirtt.utilities.IceOptionPane;
import net.dirtt.icelib.report.generalquote.GeneralQuote;
import net.dirtt.icelib.report.viewers.IEViewer;
import net.dirtt.icelib.main.commandmodules.exception.CommandException;
import net.dirtt.icebox.IceBoxMainPanel;
import net.dirtt.icelib.main.commandmodules.Command;
import icd.commands.ShowSheetCommand;
import icd.commands.AddIsometricAssemblyElevationCommand;
import javax.vecmath.Point3f;
import net.dirtt.icelib.main.commandmodules.CommandModule;
import icd.commands.AddMultipleCustomElevationCommand;
import net.dirtt.icelib.main.CustomElevationEntity;
import net.dirtt.appviews.AppView;
import net.dirtt.icelib.main.TransformableEntity;
import java.util.Vector;
import net.dirtt.icelib.main.Solution;

public class ICDCommandModule
{
    private static volatile ICDCommandModule commandModule;
    private Solution solution;
    
    private ICDCommandModule(final Solution solution) {
        this.solution = solution;
    }
    
    public static ICDCommandModule getInstance(final Solution solution) {
        if (ICDCommandModule.commandModule == null || ICDCommandModule.commandModule.solution != solution) {
            synchronized (ICDCommandModule.class) {
                if (ICDCommandModule.commandModule == null) {
                    ICDCommandModule.commandModule = new ICDCommandModule(solution);
                }
                else if (ICDCommandModule.commandModule.solution != solution) {
                    ICDCommandModule.commandModule.setSolution(solution);
                }
            }
        }
        return ICDCommandModule.commandModule;
    }
    
    private void setSolution(final Solution solution) {
        this.solution = solution;
    }
    
    public void executeAddMultipleCustomElevationCommand(final Vector<TransformableEntity> vector, final AppView appView, final Class<? extends CustomElevationEntity> clazz, final Class<?> clazz2) {
        if (vector.size() > 0) {
            new AddMultipleCustomElevationCommand(appView, vector, clazz, clazz2).scheduleExecuteAndWait(CommandModule.getIceConsole());
        }
    }
    
    public void executeAddIsometricAssemblyElevationCommand(final AppView appView, final Point3f point3f) {
        new AddIsometricAssemblyElevationCommand(appView, point3f).scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
    
    public void executeShowSheet() {
        new ShowSheetCommand(this.solution).scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
    
    public void executeShowManufacturingReport() {
        new Command(this.solution, false) {
            protected boolean doAction() throws CommandException {
                final Vector<Integer> vector = new Vector<Integer>();
                vector.add(51);
                IceBoxMainPanel.runICDExportReports(this.solution, (Vector)vector, false);
                return false;
            }
        }.scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
    
    public void executeSetSolutionDirtyAndFireModelEvents() {
        new Command(this.solution, false) {
            protected boolean doAction() throws CommandException {
                this.solution.setDirtyForSolutionRedraw();
                this.solution.fireModelEvents();
                return false;
            }
        }.scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
    
    public void executeFireModelEvents() {
        new Command(this.solution, false) {
            protected boolean doAction() throws CommandException {
                this.solution.fireModelEvents();
                return false;
            }
        }.scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
    
    public void executeExportInteriorConcepts(final IEViewer ieViewer, final GeneralQuote generalQuote) {
        new Command(this.solution, false) {
            protected boolean doAction() throws CommandException {
                final String attributeValueAsString = this.solution.getAttributeValueAsString("ICD_Record_Id", "");
                final String attributeValueAsString2 = this.solution.getAttributeValueAsString("ICD_Rev_Number", "");
                if (attributeValueAsString.equals("") || attributeValueAsString2.equals("")) {
                    IceOptionPane.showMessageDialog((Component)ieViewer, (Object)"Quote Number and Revision Level must be set.", "Export error", 0);
                    return false;
                }
                generalQuote.compileForSIFExport();
                File saveFile = IceFileUtilities.saveFile((FileFilter)new TXTFileFilter(), (Component)ieViewer);
                if (saveFile != null) {
                    if (!saveFile.getAbsolutePath().endsWith(".txt")) {
                        saveFile = new File(saveFile.getAbsolutePath() + ".txt");
                    }
                    ICDQuote.exportICD(generalQuote, saveFile, attributeValueAsString, attributeValueAsString2);
                }
                return false;
            }
        }.scheduleExecuteAndWait(CommandModule.getIceConsole());
    }
}
