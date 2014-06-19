//
//  SignUpViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "SignUpViewController.h"
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface SignUpViewController ()

@end

@implementation SignUpViewController

- (void)viewDidLoad
{
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    // set defualts
    usernameOK = false;
    PwdOK = false;
    conPwdOK = false;
    emailOK = false;
    
    self.usernameField.textField.placeholder = @"Username";
    // validate username based on length
    [self.usernameField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        if (text.length < 6) {
            field.alertView.title = @"Username is too short. Must be 6 characters or more";
            usernameOK = false;
            return NO;
        } else {
            usernameOK = true;
            return YES;
        }
    }];
    self.usernameField.delegate = self;
    
    self.pwdField.textField.placeholder = @"Password";
    // validate password based on length
    self.pwdField.textField.secureTextEntry = YES;
    [self.pwdField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        if (text.length < 8) {
            field.alertView.title = @"Password is too short. Must be 8 characters or more";
            PwdOK = false;
            return NO;
        } else {
            PwdOK = true;
            return YES;
        }
    }];
    self.pwdField.delegate = self;
    
    // comfirmed that passwords match
    self.pwdConfirmField.textField.placeholder = @"Confirm Password";
    self.pwdConfirmField.textField.secureTextEntry = YES;
    [self.pwdConfirmField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        if (![text isEqualToString:self.pwdField.textField.text]) {
            field.alertView.title = @"Password confirm doesn't match";
            conPwdOK = false;
            return NO;
        } else {
            conPwdOK = true;
            return YES;
        }
    }];
    self.pwdConfirmField.delegate = self;
    
    self.emailField.textField.placeholder = @"Email";
    [self.emailField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        NSString *emailReg = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        NSPredicate *myEmailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailReg];
        if (![myEmailTest evaluateWithObject:text]) {
            field.alertView.title = @"Invalid email address";
            emailOK = false;
            return NO;
        } else {
            emailOK = true;
            return YES;
        }
    }];
    self.emailField.delegate = self;
    
    self.movieField.textField.placeholder = @"Favorite Movie/Show";
    self.movieField.delegate = self;
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        myNetworkReachability = [Reachability reachabilityWithHostName:@"www.google.com"];
        myNetworkStatus = [myNetworkReachability currentReachabilityStatus];
        if (myNetworkStatus == NotReachable) {
            // no connection
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Network Connection"
                                                            message:@"Please check your connection and try again."
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        } else {
            // has a valid connection
            
            // check to see if all fields are valid
            if (usernameOK && PwdOK && conPwdOK && emailOK) {
                // set users values and save to Parse
                PFUser *user = [PFUser user];
                user.username = [NSString stringWithFormat:@"%@", self.usernameField.textField.text];
                user.password = [NSString stringWithFormat:@"%@", self.pwdField.textField.text];
                user.email = [NSString stringWithFormat:@"%@", self.emailField.textField.text];
                user[@"favMovie"] = [NSString stringWithFormat:@"%@", self.movieField.textField.text];
                
                [user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                    if (!error) {
                        // Hooray! Let them use the app now.
                        PFObject *movInfo = [PFObject objectWithClassName:@"movInfo"];
                        movInfo[@"user"] = [PFUser currentUser];
                        movInfo[@"watchHrs"] = [NSNumber numberWithFloat:round(hrSlider.value)];
                        [movInfo saveInBackground];
                        
                        // go to login screen
                        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
                        SignUpViewController *loginView = [storyBoard instantiateViewControllerWithIdentifier:@"FirstView"];
                        [self presentViewController:loginView animated:true completion:nil];
                    } else {
                        NSString *errorString = [error userInfo][@"error"];
                        // Show the errorString somewhere and let the user try again.
                        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                        message:errorString
                                                                       delegate:nil
                                                              cancelButtonTitle:@"OK"
                                                              otherButtonTitles:nil];
                        [alert show];
                    }
                }];
                
            } else {
                // show error alert for invalid entries
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Entry"
                                                                message:@"Please make sure your entries are valid. Anything with a red and/or grey indicator light needs to be addressed."
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
        }
            } else if (button.tag == 1) {
        // go to login screen when back button clicked
        [self dismissViewControllerAnimated:true completion:nil];
    }
}

// update hour label when slider value is changed
-(IBAction)onSlide:(id)sender {
    hrLabel.text = [[NSNumber numberWithFloat:round(hrSlider.value)] stringValue];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
