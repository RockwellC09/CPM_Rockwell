//
//  ViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "BZGFormField.h"

@interface ViewController : UIViewController <BZGFormFieldDelegate>
{
    IBOutlet UIButton *signupBtn;
    IBOutlet UIButton *loginBtn;
}

@property (weak, nonatomic) IBOutlet BZGFormField *loginUsernameField;
@property (weak, nonatomic) IBOutlet BZGFormField *loginPwdField;
-(IBAction)onClick:(id)sender;

@end
