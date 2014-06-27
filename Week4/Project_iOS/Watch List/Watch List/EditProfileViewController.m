//
//  EditProfileViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/11/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "EditProfileViewController.h"
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface EditProfileViewController ()

@end

@implementation EditProfileViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    PwdOK = false;
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    // setup password fields
    self.oldPwdField.textField.placeholder = @"Current Password";
    // validate current
    self.oldPwdField.textField.secureTextEntry = YES;
    self.oldPwdField.delegate = self;
    
    
    self.nPwdField.textField.placeholder = @"New Password";
    // validate password based on length
    self.nPwdField.textField.secureTextEntry = YES;
    [self.nPwdField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        if (text.length < 8) {
            field.alertView.title = @"Password is too short. Must be 8 characters or more";
            PwdOK = false;
            return NO;
        } else {
            PwdOK = true;
            return YES;
        }
    }];
    self.nPwdField.delegate = self;
    
    // comfirmed that passwords match
    self.pwdConfirmField.textField.placeholder = @"Confirm New Password";
    self.pwdConfirmField.textField.secureTextEntry = YES;
    [self.pwdConfirmField setTextValidationBlock:^BOOL(BZGFormField *field, NSString *text) {
        if (![text isEqualToString:self.nPwdField.textField.text]) {
            field.alertView.title = @"Password confirm doesn't match";
            PwdOK = false;
            return NO;
        } else {
            PwdOK = true;
            return YES;
        }
    }];
    self.pwdConfirmField.delegate = self;
    
    // setup movie field
    self.movieField.textField.placeholder = @"Favorite Movie/Show";
    self.movieField.delegate = self;
    
    self.movieField.textField.text = [[PFUser currentUser] objectForKey:@"favMovie"];
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    if ([prefs valueForKey:@"username"] != nil) {
        NSLog(@"get local data");
        
        self.movieField.textField.text = [NSString stringWithFormat:@"%@", [prefs valueForKey:@"fav"]];
        hrSlider.value = [[prefs valueForKey:@"hours"] intValue];
        hrLabel.text = [prefs valueForKey:@"hours"];
    } else {
        // retrieve values from Parse and populate slider
        PFQuery *query = [PFQuery queryWithClassName:@"movInfo"];
        [query whereKey:@"user" equalTo:[PFUser currentUser]];
        [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
            if (!error) {
                for (PFObject *object in objects) {
                    hrLabel.text = [NSString stringWithFormat:@"%@",[object objectForKey:@"watchHrs"]];
                    hrSlider.value = [[object objectForKey:@"watchHrs"] intValue];
                }
            } else {
                NSString *errorString = [error userInfo][@"error"];
                // show the errorString
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                message:errorString
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
            
        }];
    }
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        // check network connection
        myNetworkReachability = [Reachability reachabilityWithHostName:@"www.google.com"];
        myNetworkStatus = [myNetworkReachability currentReachabilityStatus];
        if (myNetworkStatus == NotReachable) {
            // no connection
            if (PwdOK) {
                NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
                NSString *pass = [NSString stringWithFormat:@"%@", [prefs valueForKeyPath:@"password"]];
                // compare entered password with the users password
                if ([pass isEqualToString:[NSString stringWithFormat:@"%@", self.oldPwdField.textField.text]]) {
                    [PFUser currentUser].password = self.nPwdField.textField.text;
                    [[PFUser currentUser] saveEventually];
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Password Updated!"
                                                                    message:@""
                                                                   delegate:nil
                                                          cancelButtonTitle:@"OK"
                                                          otherButtonTitles:nil];
                    [alert show];
                    //saveEventually:^(BOOL succeeded, NSError *error) {
//                        if (!error) {
//                            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Password Updated!"
//                                                                            message:@""
//                                                                           delegate:nil
//                                                                  cancelButtonTitle:@"OK"
//                                                                  otherButtonTitles:nil];
//                            [alert show];
//                        } else {
//                            NSString *errorString = [error userInfo][@"error"];
//                            // Show the errorString somewhere and let the user try again.
//                            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
//                                                                            message:errorString
//                                                                           delegate:nil
//                                                                  cancelButtonTitle:@"OK"
//                                                                  otherButtonTitles:nil];
//                            [alert show];
//                        }
//                    }];
//                } else {
//                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Wrong Password"
//                                                                    message:@"Current password is incorrect"
//                                                                   delegate:nil
//                                                          cancelButtonTitle:@"OK"
//                                                          otherButtonTitles:nil];
//                    [alert show];
                }
            } else {
                // show error alert for invalid entries
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Invalid Entry"
                                                                message:@"Please make sure your entries are valid. Anything with a red and/or grey indicator light needs to be addressed."
                                                               delegate:nil
                                                      cancelButtonTitle:@"OK"
                                                      otherButtonTitles:nil];
                [alert show];
            }
        } else {
            // has a valid connection
            
            if (PwdOK) {
                NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
                NSString *pass = [NSString stringWithFormat:@"%@", [prefs valueForKeyPath:@"password"]];
                // compare entered password with the users password
                if ([pass isEqualToString:[NSString stringWithFormat:@"%@", self.oldPwdField.textField.text]]) {
                    [PFUser currentUser].password = self.nPwdField.textField.text;
                    [[PFUser currentUser] saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
                        if (!error) {
                            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Password Updated!"
                                                                            message:@""
                                                                           delegate:nil
                                                                  cancelButtonTitle:@"OK"
                                                                  otherButtonTitles:nil];
                            [alert show];
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
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Wrong Password"
                                                                    message:@"Current password is incorrect"
                                                                   delegate:nil
                                                          cancelButtonTitle:@"OK"
                                                          otherButtonTitles:nil];
                    [alert show];
                }
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
        // check network connection
        myNetworkReachability = [Reachability reachabilityWithHostName:@"www.google.com"];
        myNetworkStatus = [myNetworkReachability currentReachabilityStatus];
        if (myNetworkStatus == NotReachable) {
            // no connection
            PFUser *cUser = [PFUser currentUser];
            // update profile
            cUser[@"favMovie"] = [NSString stringWithFormat:@"%@", self.movieField.textField.text];
            [cUser saveEventually];
            NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
            PFObject *movInfo = [PFObject objectWithoutDataWithClassName:@"movInfo" objectId:[NSString stringWithFormat:@"%@", [prefs valueForKeyPath:@"hoursID"]]];
            movInfo[@"watchHrs"] = [NSNumber numberWithFloat:round(hrSlider.value)];
            [movInfo saveEventually];
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Profile Updated"
                                                            message: nil
                                                           delegate:self
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            NSString *fav = [NSString stringWithFormat:@"%@", self.movieField.textField.text];
            [prefs setObject:fav forKey:@"fav"];
            [prefs setObject:[NSString stringWithFormat:@"%@",[NSNumber numberWithFloat:round(hrSlider.value)]] forKey:@"hours"];
            [alert show];
        } else {
            // has a valid connection
            
            PFUser *cUser = [PFUser currentUser];
            // update profile
            cUser[@"favMovie"] = [NSString stringWithFormat:@"%@", self.movieField.textField.text];
            [cUser saveInBackground];
            PFQuery *query = [PFQuery queryWithClassName:@"movInfo"];
            [query whereKey:@"user" equalTo:[PFUser currentUser]];
            [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
                if (!error) {
                    for (PFObject *object in objects) {
                        PFObject *movInfo = [PFObject objectWithoutDataWithClassName:@"movInfo" objectId:[NSString stringWithFormat:@"%@", [object objectId]]];
                        movInfo[@"watchHrs"] = [NSNumber numberWithFloat:round(hrSlider.value)];
                        [movInfo saveInBackground];
                        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Profile Updated"
                                                                        message: nil
                                                                       delegate:self
                                                              cancelButtonTitle:@"OK"
                                                              otherButtonTitles:nil];
                        NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
                        NSString *fav = [NSString stringWithFormat:@"%@", self.movieField.textField.text];
                        [prefs setObject:fav forKey:@"fav"];
                        [prefs setObject:[NSString stringWithFormat:@"%@",[NSNumber numberWithFloat:round(hrSlider.value)]] forKey:@"hours"];
                        [alert show];
                    }
                } else {
                    NSString *errorString = [error userInfo][@"error"];
                    // show the errorString
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                    message:errorString
                                                                   delegate:nil
                                                          cancelButtonTitle:@"OK"
                                                          otherButtonTitles:nil];
                    [alert show];
                }
                
            }];
        }
        
    } else {
        [self dismissViewControllerAnimated:true completion:nil];
    }
}

// update hour label when slider value is changed
-(IBAction)onSlide:(id)sender {
    hrLabel.text = [[NSNumber numberWithFloat:round(hrSlider.value)] stringValue];
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    EditProfileViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
    [self presentViewController:profileView animated:true completion:nil];
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
