
var gaMonthNames = new Array(
  new Array(i18n.Jan,i18n.Feb,i18n.Mar,i18n.Apr,i18n.May,i18n.Jun,i18n.Jul,i18n.Aug,i18n.Sep,i18n.Oct,i18n.Nov,i18n.Dec),
  new Array(i18n.Jan_Abridge,i18n.Feb_Abridge,i18n.Mar_Abridge,i18n.Apr_Abridge,i18n.May_Abridge,i18n.Jun_Abridge,
                    i18n.Jul_Abridge,i18n.Aug_Abridge,i18n.Sep_Abridge,i18n.Oct_Abridge,i18n.Nov_Abridge,i18n.Dec_Abridge)
  );
var gaDayNames = new Array( 
  new Array('S', 'M', 'T', 'W', 'T', 'F', 'S'),
  new Array(i18n.sunday_Abridge,i18n.monday_Abridge,i18n.tuesday_Abridge,i18n.wednesday_Abridge,i18n.thursday_Abridge,i18n.friday_Abridge,i18n.saturday_Abridge),
  new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday')
  );
var gaMonthDays = new Array( 
   /* Jan */ 31,     /* Feb */ 29, /* Mar */ 31,     /* Apr */ 30, 
   /* May */ 31,     /* Jun */ 30, /* Jul */ 31,     /* Aug */ 31, 
   /* Sep */ 30,     /* Oct */ 31, /* Nov */ 30,     /* Dec */ 31 )
var StyleInfo            = null
var goStyle              = new Object()
var gaDayCell            = new Array()
var goDayTitleRow        = null
var goYearSelect         = null
var goMonthSelect        = null
var goCurrentDayCell     = null
var giStartDayIndex      = 0
var gbLoading            = true
var giDay
var giMonth
var giYear
var giMonthLength        = 1
var giDayLength          = 1
var giFirstDay           = 0
var gsGridCellEffect     = 'raised'
var gsGridLinesColor     = 'black'
var gbShowDateSelectors  = true
var gbShowDays           = true
var gbShowTitle          = true
var gbShowHorizontalGrid = true
var gbShowVerticalGrid   = true
var gbValueIsNull        = false
var gbReadOnly           = false
var giMinYear            = 1900
var giMaxYear            = 2099




function fnGetValue()
{
  var sValue
  var sTmp
  if (gbValueIsNull) return null
  if (giYear < 10) sTmp= '000' + giYear
  if (giYear < 100) sTmp= '00' + giYear
  if (giYear < 1000) sTmp= '0' + giYear
  sTmp= giYear
  sValue = sTmp +'-'+
           ((giMonth < 10) ? '0' + giMonth : giMonth) + '-' +((giDay < 10) ? '0' + giDay : giDay)
  return sValue
}

function fnCreateCalendarHTML()
{
  var row, cell

  element.innerHTML = 
  '<table border=0 cellpadding=0 cellspacing=0  background="images/day_title.gif" align=center class=WholeCalendar_' + uniqueID + '> ' +
  '  <tr style="borderbottom:1 solid black">                                          ' +
  '      <td class=Title_' + uniqueID + '  ></td>  ' +
  '      <td class=DateControls_' + uniqueID + ' > ' +
  '        <nobr> <select style="width: 80px;"></select>                ' +
  '               <select style="width: 80px;"></select>                ' +
  '               <input type=button name=button1  value="' + i18n.sure +'" class=enter LANGUAGE=javascript onclick="return button1_onclick()" style="border-width:1px; border-color:#672301;height:20px; background-color:#e5854c; color:#ffffff;"></input>'+
  ' </nobr> </td>  ' +
  '  </tr>                                         ' +
  '  <tr> <td colspan=3>                           ' +
  '    <table align=center valign=center class=CalTable_' + uniqueID + ' cellspacing=0 border=0> ' +
  '      <tr><td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td>' +
  '          <td style="BORDER-TOP: 1px solid DimGray " class=DayTitle_' + uniqueID + '></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '      <tr ><td height="23"></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>' +
  '    </table> ' +
  '  </tr>      ' +
  '</table>     ';

  goDayTitleRow = element.children[0].rows[1].cells[0].children[0].rows[0]
  goMonthSelect = element.children[0].rows[0].cells[1].children[0].children[1]
  goYearSelect = element.children[0].rows[0].cells[1].children[0].children[0]

  var table = jQuery(element).children().children().children().children().children("table")[0];
  for (row=1; row < 7; row++)
    for (cell=0; cell < 7; cell++){
         gaDayCell[((row-1)*7) + cell] = table.rows[row].cells[cell]
    }
}


function fnGetPropertyDefaults()
{
  var x
  var oDate = new Date()

  giDay = oDate.getDate()
  giMonth = oDate.getMonth() + 1
  giYear = oDate.getYear()
  if (giYear < 1000) giYear += 1900   
  if (element.year) 
  {
    if (! isNaN(parseInt(element.year))) giYear = parseInt(element.year)
    if (giYear < giMinYear) giYear = giMinYear
    if (giYear > giMaxYear) giYear = giMaxYear
  }
  fnCheckLeapYear(giYear)
  if (element.month) 
  {
    if (! isNaN(parseInt(element.month))) giMonth = parseInt(element.month)
    if (giMonth < 1) giMonth = 1
    if (giMonth > 12) giMonth = 12
  }
  if (element.day) 
  {
    if (! isNaN(parseInt(element.day))) giDay = parseInt(element.day)
    if (giDay < 1) giDay = 1
    if (giDay > gaMonthDays[giMonth - 1]) giDay = gaMonthDays[giMonth - 1]
  }
  if (element.monthLength)
  {
    switch (element.monthLength.toLowerCase())
    {
      case 'short' :
        giMonthLength = 0
        break
      case 'long' :
        giMonthLength = 1
        break
    }
  }
  if (element.dayLength)
  {
    switch (element.dayLength.toLowerCase())
    {
      case 'short' :
        giDayLength = 0
        break
      case 'medium' :
        giDayLength = 1
        break
      case 'long' :
        giDayLength = 1
        break
    }
  }
  if (element.firstDay)
  {
    if ((element.firstDay >= 0) && (element.firstDay <= 6))
      giFirstDay = element.firstDay
  }
  if (element.gridCellEffect) 
  { 
    switch (element.gridCellEffect.toLowerCase())
    {
      case 'raised' :
        giGridCellEffect = 'raised'
        break
      case 'flat' :
        giGridCellEffect = 'flat'
        break
      case 'sunken' :
        giGridCellEffect = 'sunken'
        break
    }
  }
  if (element.gridLinesColor) 
    gsGridLinesColor = element.gridLinesColor
   if (element.showDateSelectors)
    gbShowDateSelectors = (element.showDateSelectors) ? true : false
   if (element.showDays)
    gbShowDays = (element.showDays) ? true : false
  if (element.showTitle)
    gbShowTitle = (element.showTitle) ? true : false
  if (element.showHorizontalGrid)
    gbShowHorizontalGrid = (element.showHorizontalGrid) ? true : false
  if (element.showVerticalGrid)
    gbShowVerticalGrid = (element.showVerticalGrid) ? true : false
  if (element.valueIsNull)
    gbValueIsNull = (element.valueIsNull) ? true : false
  if (element.name)
    gsName = element.name
  if (element.readOnly)
    gbReadOnly = (element.readOnly) ? true : false
}
function fnSetDate(iDay, iMonth, iYear)
{
  var bValueChange = false;
  if (gbValueIsNull)
  { 
    gbValueIsNull = false
    fnOnPropertyChange("propertyName", "valueIsNull")
  }
  if (iYear < giMinYear) iYear = giMinYear
  if (iYear > giMaxYear) iYear = giMaxYear
  if (giYear != iYear)
  {
    fnCheckLeapYear(iYear)
  }
  if (iMonth < 1) iMonth = 1
  if (iMonth > 12) iMonth = 12
  if (iDay < 1) iDay = 1
  //if (iDay > gaMonthDays[giMonth - 1]) iDay = gaMonthDays[giMonth - 1]
  if ((giDay == iDay) && (giMonth == iMonth) && (giYear == iYear))
    return
  else
    bValueChange = true
  if (giDay != iDay) 
  {
    giDay = iDay
    fnOnPropertyChange("propertyName", "day")
  }
  if ((giYear == iYear) && (giMonth == iMonth))
  {
    goCurrentDayCell.className = 'Day_' + uniqueID
    goCurrentDayCell = gaDayCell[ iDay - 1 + giStartDayIndex ]
    goCurrentDayCell.className = 'DaySelected_' + uniqueID
    giDay = iDay
  }
  else 
  {
    if (giYear != iYear)
    {
      giYear = iYear
      fnOnPropertyChange("propertyName", "year")
      fnUpdateYearSelect()
    }
    if (giMonth != iMonth) 
    {
      giMonth = iMonth
      fnOnPropertyChange("propertyName", "month")
      fnUpdateMonthSelect()
    }
    fnUpdateTitle()   
    fnFillInCells()
  }
  if (bValueChange) fnOnPropertyChange("propertyName", "value")
}
function fnUpdateTitle()
{
  var oTitleCell = element.children[0].rows[0].cells[0]
  if (gbShowTitle)
    oTitleCell.innerHTML = gaMonthNames[giMonthLength][giMonth - 1] + " " + giYear
  else 
    oTitleCell.innerText = ' '
}
function fnUpdateDayTitles()
{
  var dayTitleRow = jQuery(element).children().children().children()[0]//.children().children("table")[0];//element.children[0].rows[1].cells[0].children[0].rows[0]
  var iCell = 0
  for (i=giFirstDay ; i < 7 ; i++)
  {
    jQuery(goDayTitleRow.cells[iCell++]).append( gaDayNames[giDayLength][i]);
  }
  for (i=0; i < giFirstDay; i++)
  {
    jQuery(goDayTitleRow.cells[iCell++]).append( gaDayNames[giDayLength][i]);
  }
}
function fnUpdateMonthSelect()
{
  goMonthSelect.options[ giMonth - 1 ].selected = true
}
function fnBuildMonthSelect()
{
	  jQuery(goMonthSelect).empty().bind("change",fnMonthSelectOnChange);
	  for (i=0 ; i < 12; i++)
	  {
	    jQuery(goMonthSelect).append("<option>" + gaMonthNames[giMonthLength][i] + "</option>")
	  }
	  goMonthSelect.options[ giMonth - 1 ].selected = true
}
function fnMonthSelectOnChange()
{
  iMonth = goMonthSelect.selectedIndex + 1
  fnSetDate(giDay, iMonth, giYear)
}
function fnUpdateYearSelect()
{
  goYearSelect.options[ giYear - giMinYear ].selected = true
}
function fnBuildYearSelect()
{
   jQuery(goYearSelect).empty().bind("change",fnYearSelectOnChange);
      for (i=giMinYear; i <= giMaxYear; i++)
      {
        jQuery(goYearSelect).append("<option>" + i + "</option>")
      }
      goYearSelect.options[ giYear-giMinYear ].selected = true
}
function fnYearSelectOnChange()
{
  iYear = goYearSelect.selectedIndex + giMinYear
  fnSetDate(giDay, giMonth, iYear)
}
function fnCheckLeapYear(iYear)
{
  gaMonthDays[1] = (((!(iYear % 4)) && (iYear % 100) ) || !(iYear % 400)) ? 29 : 28
}
function fnFillInCells()
{
  var iDayCell = 0
  var iLastMonthIndex, iNextMonthIndex
  var iLastMonthTotalDays
  var iStartDay
  fnCheckLeapYear(giYear)
  iLastMonthDays = gaMonthDays[ ((giMonth - 1 == 0) ? 12 : giMonth - 1) - 1]
  iNextMonthDays = gaMonthDays[ ((giMonth + 1 == 13) ? 1 : giMonth + 1) - 1]
  iLastMonthYear = (giMonth == 1)  ? giYear - 1 : giYear
  iLastMonth     = (giMonth == 1)  ? 12         : giMonth - 1 
  iNextMonthYear = (giMonth == 12) ? giYear + 1 : giYear
  iNextMonth     = (giMonth == 12) ? 1          : giMonth + 1 
  var oDate = new Date(giYear, (giMonth - 1), 1)
  iStartDay = oDate.getDay() - giFirstDay
  if (iStartDay < 1) iStartDay += 7
  iStartDay = iLastMonthDays - iStartDay + 1
  for (i = iStartDay ; i <= iLastMonthDays  ; i++ , iDayCell++)
  {
     if (gaDayCell[iDayCell].className != 'OffDay_' + uniqueID)
     gaDayCell[iDayCell].className = 'OffDay_' + uniqueID
     jQuery(gaDayCell[iDayCell]).empty().append(i).attr("day",i).attr("month",iLastMonth).attr("year",iLastMonthYear);
  }
  giStartDayIndex = iDayCell
  for (i = 1 ; i <= gaMonthDays[giMonth - 1] ; i++, iDayCell++)
  {
     if (giDay == i)
     {
       goCurrentDayCell = gaDayCell[iDayCell]
       gaDayCell[iDayCell].className = 'DaySelected_' + uniqueID
     } 
     else 
     {
       if (gaDayCell[iDayCell].className != 'Day_' + uniqueID)
         gaDayCell[iDayCell].className = 'Day_' + uniqueID
     }
     jQuery(gaDayCell[iDayCell]).empty().append(i).attr("day",i).attr("month",giMonth).attr("year",giYear);
  }
  for (i = 1 ; iDayCell < 42 ; i++, iDayCell++)
  {
     if (gaDayCell[iDayCell].className != 'OffDay_' + uniqueID)
       gaDayCell[iDayCell].className = 'OffDay_' + uniqueID
     jQuery(gaDayCell[iDayCell]).empty().append(i).attr("day",i).attr("month",iNextMonth).attr("year",iNextMonthYear);
  }
}
function fnOnClick(event)
{
  var e = event.srcElement?event.srcElement:event.target;
  if (e.tagName == "TD") 
  {
    if (gbReadOnly || (!jQuery(e).attr("day"))) return  // The calendar is read only
    if ((jQuery(e).attr("year") < giMinYear) || (jQuery(e).attr("year") > giMaxYear)) return
    fnSetDate(jQuery(e).attr("day"), jQuery(e).attr("month"), jQuery(e).attr("year"))
  }
}
function fnOnSelectStart()
{
  window.event.returnValue = false
  window.event.cancelBubble = true
}
function fnOnReadyStateChange()
{
  gbLoading = (readyState != "complete")
}
function fnOnPropertyChange(name,value)
{
    //delete old code , see calendar.htc
    //do nothing
}

function fnUpdateGridColors()
{
  
}
