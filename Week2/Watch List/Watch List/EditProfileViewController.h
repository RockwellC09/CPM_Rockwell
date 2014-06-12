//
//  EditProfileViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/11/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BZGFormField.h"
#import <Parse/Parse.h>


@interface EditProfileViewController : UIViewController <BZGFormFieldDelegate, UIAlertViewDelegate>
{
    IBOutlet UISlider *hrSlider;
    IBOutlet UILabel *hrLabel;
    BOOL PwdOK;
}
@property (weak, nonatomic) IBOutlet BZGFormField *oldPwdField;
@property (weak, nonatomic) IBOutlet BZGFormField *nPwdField;
@property (weak, nonatomic) IBOutlet BZGFormField *pwdConfirmField;
@property (weak, nonatomic) IBOutlet BZGFormField *movieField;
-(IBAction)onClick:(id)sender;
-(IBAction)onSlide:(id)sender;

@end
