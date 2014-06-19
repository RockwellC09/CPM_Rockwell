//
//  SignUpViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "BZGFormField.h"
#import <Parse/Parse.h>
#import "Reachability.h"

@interface SignUpViewController : UIViewController <BZGFormFieldDelegate>
{
    BOOL usernameOK;
    BOOL PwdOK;
    BOOL conPwdOK;
    BOOL emailOK;
    IBOutlet UISwitch *movieSwitch;
    IBOutlet UISlider *hrSlider;
    IBOutlet UILabel *hrLabel;
}

@property (weak, nonatomic) IBOutlet BZGFormField *usernameField;
@property (weak, nonatomic) IBOutlet BZGFormField *pwdField;
@property (weak, nonatomic) IBOutlet BZGFormField *pwdConfirmField;
@property (weak, nonatomic) IBOutlet BZGFormField *emailField;
@property (weak, nonatomic) IBOutlet BZGFormField *movieField;
-(IBAction)onClick:(id)sender;
-(IBAction)onSlide:(id)sender;
@end
