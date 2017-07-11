# ChipCloud
[![Release](https://jitpack.io/v/arjunmn/ChipCloud.svg)](https://jitpack.io/#arjunmn/ChipCloud) [![license](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://github.com/arjunmn/ChipCloud/blob/master/LICENSE)

This is a mirror of the [ChipCloud](https://github.com/adroitandroid/ChipCloud) fork maintained by [adroitandroid](https://github.com/adroitandroid/). It is intended primarily for personal use to experiment with different variations and project-specific feature needs. Part of the original documentation from [adroitandroid](https://github.com/adroitandroid/) below.

--

The ChipCloud library was originally a (very) quickly knocked up Android view for some larger hackathon project by [fiskurgit](https://github.com/fiskurgit). It creates a wrapping cloud of material '[Chips](https://www.google.com/design/spec/components/chips.html)'. 

![Trainer Sizes](images/trainer_sizes.png)

## Usage

Add to your Android layout xml:
```xml
<com.arjunmn.chipcloud.ChipCloud
    android:id="@+id/chip_cloud"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

Configure in xml:  
```xml
<com.arjunmn.chipcloud.ChipCloud
    xmlns:chipcloud="http://schemas.android.com/apk/res-auto"
    android:id="@+id/chip_cloud"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    chipcloud:deselectedColor="@color/deselected_color"
    chipcloud:deselectedFontColor="@color/deselected_font_color"
    chipcloud:selectedColor="@color/selected_color"
    chipcloud:selectedFontColor="@color/selected_font_color"
    chipcloud:deselectTransitionMS="500"
    chipcloud:selectTransitionMS="750"
    chipcloud:labels="@array/labels"
    chipcloud:selectMode="required"
    chipcloud:allCaps="true"
    chipcloud:gravity="staggered"
    chipcloud:minHorizontalSpacing="32dp"
    chipcloud:verticalSpacing="16dp"
    chipcloud:textSize="14sp"    
    chipcloud:typeface="RobotoSlab-Regular.ttf"/> <!--path relative to assets folder-->
```
or in code:  
```java
ChipCloud chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);

new ChipCloud.Configure()
        .chipCloud(chipCloud)
        .selectedColor(Color.parseColor("#ff00cc"))
        .selectedFontColor(Color.parseColor("#ffffff"))
        .deselectedColor(Color.parseColor("#e1e1e1"))
        .deselectedFontColor(Color.parseColor("#333333"))
        .selectTransitionMS(500)
        .deselectTransitionMS(250)
        .labels(someStringArray)
        .mode(ChipCloud.Mode.MULTI)
        .allCaps(false)
        .gravity(ChipCloud.Gravity.CENTER)
        .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
        .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
        .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
        .typeface(Typeface.createFromAsset(getContext().getAssets(), "RobotoSlab-Regular.ttf"))
        .chipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                //...
            }
            @Override
            public void chipDeselected(int index) {
                //...
            }
        })
        .build();
```

Default value of textSize is equivalent to 13sp, of verticalSpacing is equivalent to 8dp, of minHorizontalSpacing is equivalent to 8dp, of allCaps is false.

All chips have a height of 32dp and a padding of 12dp on left and right within the chip, [as per material guidelines](https://www.google.com/design/spec/components/chips.html).

Add items dynamically too:
```java
chipCloud.addChip("Foo");
chipCloud.addChip("Bar");

//or

chipCloud.addChips(someStringArray);
```

Set the selected index using ```chipCloud.setSelectedChip(2)```

Real-world example for shoe sizes:  
![Shoe Sizes](images/wrapping_example.png)

Labels can also be updated in-place, e.g.

from ![Before](images/label_update_before.png) to ![After](images/label_update_after.png)
using ```update()``` as illustrated below

```java
new ChipCloud.Configure().chipCloud(binding.chipCloud).labels(newChipLabels).update();
```

## Modes

```java
public enum Mode {
  SINGLE, MULTI, REQUIRED, NONE
}
```

The default mode is single choice (where it's valid to have no chip selected), if you want a RadioGroup manadatory style where once a chip is selected there must always be a selected item use ```chipCloud.setMode(ChipCloud.Mode.REQUIRED);``` (or set in xml or the builder). There's a multiple select mode too: ```chipCloud.setMode(ChipCloud.Mode.MULTIPLE);```. If you want to deactiviate selecting of chips you can set the select mode to ```chipCloud.setMode(ChipCloud.Mode.NONE);```.

## Gravity

```java
public enum Gravity {
  LEFT, RIGHT, CENTER, STAGGERED
}
```

The default gravity is LEFT. CENTER and STAGGERED are similar except that CENTER leaves only minimum horizontal spacing between the chips whereas in STAGGERED chips occupy the available space equally while having at least minimum horizontal spacing between them.

| ![Left](images/gravity_left.png) | ![Right](images/gravity_right.png) | ![Center](images/gravity_center.png) | ![Staggered](images/gravity_staggered.png) |
|:---:|:---:|:---:|:---:|
| LEFT | RIGHT | CENTER | STAGGERED |

## Dependency

Add jitpack.io to your root build.gradle, eg:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

then add the dependency to your project build.gradle:

```groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.github.adroitandroid:ChipCloud:2.2.1'
}
```
You can find the latest version in the releases tab above: https://github.com/adroitandroid/ChipCloud/releases

## Licence

Full licence here: https://github.com/arjunmn/ChipCloud/blob/master/LICENSE

In short:

> The MIT License is a permissive license that is short and to the point. It lets people do anything they want with your code as long as they provide attribution back to you and don’t hold you liable.
