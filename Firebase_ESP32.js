function firebase() {
  var base = FirebaseApp.getDatabaseByUrl("PROJECT_ID", "PROJECT_KEY");
  Logger.log(base.getData());
  var sheet = SpreadsheetApp.openById("SPREADSHEET_ID");
  var User = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/User");
  sheet.getRange("A2").setValue(User);
  var chargingState = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Charging Status");
  sheet.getRange("C2").setValue(chargingState);
  var finishState = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Finished Charging");
  

  // Wait for chargingState to be 1
  while(chargingState == false)
  {
    chargingState = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Charging Status");
  }
    Utilities.sleep(4000);   // Wait for initial SOC to be updated in Firebase.
    var SOC = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC");
    sheet.getRange("E2").setValue(SOC);
    while(finishState == false)
    {
      SOC = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/SOC");
      finishState = base.getData("4S764vco0MW5Ccx1GPoqMrSC5Sp2/Finished Charging");
    }
    sheet.getRange("D2").setValue(finishState);
    sheet.getRange("F2").setValue(SOC);
    var totalEnergy = ((sheet.getRange("F2").getValue() - sheet.getRange("E2").getValue())/100)*70;  // Total Energy in kWh
    sheet.getRange("G2").setValue(totalEnergy);

    // Call Billing function
      var sourceSpreadsheet = SpreadsheetApp.openById("INVOICE_SHEET_ID");    // Invoice spreadsheet ID
      sourceSpreadsheet.getRange("D9").setValue(Utilities.formatDate(new Date(),"IST","dd-MM-yyyy"));
      sourceSpreadsheet.getRange("B12").setValue(User);
      sourceSpreadsheet.getRange("E19").setValue(totalEnergy);

    var pdfName = "Invoice_" + sheet.getRange("A2").getValue();
    
    // Generate pdf function
      // Get active sheet.
  var sheets = sourceSpreadsheet.getSheets();
  var sheetName = sourceSpreadsheet.getActiveSheet().getName();
  var sourceSheet = sourceSpreadsheet.getSheetByName(sheetName);
  
  // Set the output filename as SheetName.
  var pdfName = sheetName + "_" + sourceSpreadsheet.getRange("B12").getValue();

  // Get folder containing spreadsheet to save pdf in.
  var parents = DriveApp.getFileById(sourceSpreadsheet.getId()).getParents();
  if (parents.hasNext()) {
    var folder = parents.next();
  }
  else {
    folder = DriveApp.getRootFolder();
  }
  
  // Copy whole spreadsheet.
  var destSpreadsheet = SpreadsheetApp.open(DriveApp.getFileById(sourceSpreadsheet.getId()).makeCopy("tmp_convert_to_pdf", folder))

  // Delete redundant sheets.
  var sheets = destSpreadsheet.getSheets();
  for (i = 0; i < sheets.length; i++) {
    if (sheets[i].getSheetName() != sheetName){
      destSpreadsheet.deleteSheet(sheets[i]);
    }
  }
  
  var destSheet = destSpreadsheet.getSheets()[0];

  // Repace cell values with text (to avoid broken references).
  var sourceRange = sourceSheet.getRange(1,1,sourceSheet.getMaxRows(),sourceSheet.getMaxColumns());
  var sourcevalues = sourceRange.getValues();
  var destRange = destSheet.getRange(1, 1, destSheet.getMaxRows(), destSheet.getMaxColumns());
  destRange.setValues(sourcevalues);

  // Save to pdf.
  var theBlob = destSpreadsheet.getBlob().getAs('application/pdf').setName(pdfName);
  var newFile = folder.createFile(theBlob);

  // Delete the temporary sheet.
  DriveApp.getFileById(destSpreadsheet.getId()).setTrashed(true);

  // Mail the bill
    var filename = DriveApp.getFileById("1sKZDwOKsrdFiIvw-vyDpl4Mcw_wMOBlihmrpd6KrnZs").getAs("application/pdf");
    filename.setName(pdfName + ".pdf"); 
    var message = 'Dear ' + sheet.getRange("A2").getValue() + ", \n The bill for your charging session has been attached with this mail. \n Thank you for charging. Please visit again";
   var subject = 'Invoice for' + sheet.getRange("A2").getValue();
   GmailApp.sendEmail("srivathsjm.ec17@rvce.edu.in", subject,message,{attachments: [filename]});
  
}
