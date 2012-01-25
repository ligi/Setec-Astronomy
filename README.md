What is it
==========

Sometimes you want to give somebody your phone or your tablet - for example to give a Hands-On experience on your App. But these Devices often contain highly sensitive Information ( Your Mail, Notes, Calendar-entrys, ..) 
One often gets nervous whether the one you gave the device is really just doing the thing you expect him to do.
Setec Astronomy counter-acts when certain Packages are used - e.g. email / calendar

How does it work
================

The APP is watching the System-Log for indications that an app is started or brought to background. You can configure Actions on these events like killing the app or playing a sound. 

Known Issues
============

Unprotected Information leaks
-----------------------------
 * widgets
 * notification

ICS
---
on ICS killing works only reliable if settings > developer options > Don't keep Activitys is selected
