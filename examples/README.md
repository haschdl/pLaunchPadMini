# Launch Pad MINI - Examples

* [Basics 1 - LED Colors](#basics-1---LED-colors)
* [Basics 2 - Pad mode](#basics-2---pad-mode)
* [Advanced 1 - Audio Analyzer](#advanced-1--audio-analyzer)

## Basics 1 - LED Colors

File:  Basics_1_LED_Colors.pde

Run the sketch and click on the sketch to switch across the examples. 

### 4: Animation

``` Java
x = 3;
y = 3;
int m = Math.round(1.5 * (1 + cos((float)frameCount*TWO_PI/10)));

controller.setLedColor(x-m, y-m, LED_COLOR.getRandom(true));
controller.setLedColor(x-m, y+1+m, LED_COLOR.getRandom(true));
controller.setLedColor(x+1+m, y-m, LED_COLOR.getRandom(true));
controller.setLedColor(x+1+m, y+1+m, LED_COLOR.getRandom(true));
```

![Example 4](../.github/assets/LaunchpadMin_Example_4.gif)



### 5: Looping

 Looping in first row with `setLedColor(column, row, color)`.

```Java
controller.reset();
int col = frameCount % 8;
controller.setLedColor(col, 0, LED_COLOR.GREEN_FULL); 
```
![Example 5](../.github/assets/LaunchpadMin_Example_5.gif)

### 6: Looping

Looping across whole matrix with random color (`LED_COLOR.getRandom()`).

![Example 6](../.github/assets/LaunchpadMin_Example_6.gif)

## Basics 2 - Pad mode

![Example 2](../.github/assets/example-padmode-loop.jpg)

## Advanced 1 - Audio analyzer