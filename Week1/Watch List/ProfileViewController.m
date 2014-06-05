//
//  ProfileViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/5/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "ProfileViewController.h"

@interface ProfileViewController ()

@end

@implementation ProfileViewController

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
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    BOOL hasFavMovie;
    
    if ([[[PFUser currentUser] objectForKey:@"hasFavMovie"] isEqualToString:@"true"]) {
        hasFavMovie = true;
    } else {
        hasFavMovie = false;
    }
    userLabel.text = [[PFUser currentUser] objectForKey:@"username"];
    emailLabel.text = [[PFUser currentUser] objectForKey:@"email"];
    if (hasFavMovie) {
        favLabel.text = [[PFUser currentUser] objectForKey:@"favMovie"];
    } else {
        favLabel.text = @"None";
    }
    hrsLabel.text = [[PFUser currentUser] objectForKey:@"watchHrs"];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)onClick:(id)sender {
    [PFUser logOut];
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    ProfileViewController *loginView = [storyBoard instantiateViewControllerWithIdentifier:@"FirstView"];
    [self presentViewController:loginView animated:true completion:nil];
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
