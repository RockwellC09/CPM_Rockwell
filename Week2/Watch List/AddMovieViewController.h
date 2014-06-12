//
//  AddMovieViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "BZGFormField.h"


@interface AddMovieViewController : UIViewController <UIPickerViewDelegate, UIPickerViewDataSource, BZGFormFieldDelegate, UIAlertViewDelegate>
{
    NSArray *days;
    IBOutlet UIDatePicker *timePicker;
    IBOutlet UIPickerView *dayPicker;
}

@property (weak, nonatomic) IBOutlet BZGFormField *itemTitle;
-(IBAction)onClick:(id)sender;
@end
