//
//  ViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/4/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ViewController.h"

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

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        [PFUser logInWithUsernameInBackground:[NSString stringWithFormat:@"%@", self.loginUsernameField.textField.text] password:[NSString stringWithFormat:@"%@", self.loginPwdField.textField.text]
                                        block:^(PFUser *user, NSError *error) {
                                            if (user) {
                                                // Do stuff after successful login.
                                                UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
                                                
                                                ViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
                                                [self presentViewController:profileView animated:true completion:nil];
                                            } else {
                                                // The login failed. Check error to see why.
                                                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                                                message:@"Please check your login credentials and try again."
                                                                                               delegate:nil
                                                                                      cancelButtonTitle:@"OK"
                                                                                      otherButtonTitles:nil];
                                                [alert show];
                                            }
                                        }];
    } else if (button.tag == 1) {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        ViewController *signupView = [storyBoard instantiateViewControllerWithIdentifier:@"SignUpView"];
        [self presentViewController:signupView animated:true completion:nil];
    }
}



- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

@end
