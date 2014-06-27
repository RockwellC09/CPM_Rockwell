//
//  ViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ViewController.h"
Reachability *myNetworkReachability;
NetworkStatus myNetworkStatus;

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    // prepare login fields
    self.loginUsernameField.textField.placeholder = @"Username";
    self.loginUsernameField.delegate = self;
    self.loginPwdField.textField.placeholder = @"Password";
    self.loginPwdField.textField.secureTextEntry = YES;
    self.loginPwdField.delegate = self;
    
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

// called when login button or sign up button clicked
-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        // attempt to log user in
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
            [PFUser logInWithUsernameInBackground:[NSString stringWithFormat:@"%@", self.loginUsernameField.textField.text] password:[NSString stringWithFormat:@"%@", self.loginPwdField.textField.text]
                                            block:^(PFUser *user, NSError *error) {
                                                if (user) {
                                                    // save user password to compare for update password later
                                                    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
                                                    NSString *userPassword = [NSString stringWithFormat:@"%@", self.loginPwdField.textField.text];
                                                    [prefs setObject:userPassword forKey:@"password"];
                                                    
                                                    myNetworkReachability = [Reachability reachabilityWithHostName:@"www.google.com"];
                                                    myNetworkStatus = [myNetworkReachability currentReachabilityStatus];
                                                    if (myNetworkStatus == NotReachable) {
                                                        // no connection
                                                        [prefs setObject:@"no" forKey:@"firstRun"];
                                                    } else {
                                                        [prefs setObject:@"yes" forKey:@"firstRun"];
                                                    }
                                                    
                                                    // Do stuff after successful login.
                                                    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
                                                    
                                                    ViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
                                                    [self presentViewController:profileView animated:true completion:nil];
                                                } else {
                                                    // The login failed and displayed error message
                                                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                                                    message:@"Please check your login credentials and try again."
                                                                                                   delegate:nil
                                                                                          cancelButtonTitle:@"OK"
                                                                                          otherButtonTitles:nil];
                                                    [alert show];
                                                }
                                            }];
        }
        
    } else if (button.tag == 1) {
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
            UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
            
            ViewController *signupView = [storyBoard instantiateViewControllerWithIdentifier:@"SignUpView"];
            [self presentViewController:signupView animated:true completion:nil];
        }
    }
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

@end
