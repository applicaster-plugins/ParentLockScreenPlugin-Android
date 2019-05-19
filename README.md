# IAI Web Plugin

### Description

IAI web plugin enable to add token to the headers while loading a new web page. In addition you can
also invoke the login page for a specific link while adding the is_lock param and set it to true.

### How to use it

In order to use this plugin we need to use a url scheme with the web page link.

for example:
```
iai://plugin?type=general&plugin_identifier=iai_web&url=<url_encoded_link>&is_lock=<true/false>&title=<some_text>
```
## params:

* **url** - the web page we want to load ( must be url encoded )
* **is_lock** - force login before web page is displayed. default is false.


## Android configuration

* **top bar bg color**  - configure **top_bar_background** in Zapp.
* **x button image** - upload asset to **keys up_arrow** and **up_arrow_pressed** in Zapp
* **title style** - configure **TopBarTitleText** in Zapp.
